# Gerenciamento de Eventos com microserviços

## Descrição do projeto

Este repositório contém um projeto acadêmico de gerenciamento de eventos desenvolvido com Java, Spring Boot e Maven. A solução possui dois microservices de negócio independentes, um Discovery Server e um API Gateway.

O sistema permite cadastrar, consultar, atualizar, publicar, cancelar e excluir eventos. Quando um evento é publicado ou cancelado, o eventos-service comunica-se com o notificacoes-service para produzir e armazenar uma notificação simulada.

## Problema que o sistema resolve

A solução centraliza o gerenciamento do ciclo de vida de eventos e separa a responsabilidade de notificação da responsabilidade principal de cadastro. Dessa forma, o gerenciamento do evento continua funcionando mesmo quando o serviço secundário de notificações apresenta indisponibilidade.

Os usuários principais são organizadores e administradores de eventos que precisam registrar informações, acompanhar estados e manter um histórico das notificações produzidas.

## Arquitetura

O API Gateway é o ponto único de entrada para as chamadas externas. Os serviços registram seus nomes e endereços no Eureka. O eventos-service localiza o notificacoes-service pelo nome registrado, sem utilizar uma URL fixa.

## Serviços

O eventos-service será responsável pelo cadastro, consulta, alteração, publicação e cancelamento dos eventos. Seu banco de dados será o PostgreSQL, utilizando o database lógico eventos_db.

O notificacoes-service será responsável pela criação e pelo armazenamento do histórico de notificações relacionadas aos eventos. Seu banco será o MongoDB, utilizando o database lógico notificacoes_db.

O discovery-server será responsável pelo registro e pela descoberta dinâmica dos serviços. O api-gateway será o ponto único de entrada para as chamadas externas.

## Bancos de dados

| Microservice | Banco | Database lógico |
|---|---|---|
| eventos-service | PostgreSQL 17 | eventos_db |
| notificacoes-service | MongoDB 8 | notificacoes_db |

Cada microserviço possui seu próprio banco de dados lógico. O eventos-service não acessa diretamente o MongoDB e o notificacoes-service não acessa o PostgreSQL. Não existem joins ou chaves estrangeiras entre os bancos.

## Justificativa para o MongoDB

O MongoDB foi escolhido para o notificacoes-service porque as notificações são registros independentes, predominantemente gravados e consultados como documentos completos. Os metadados podem variar conforme o tipo da notificação e o canal utilizado, sem exigir a criação de novas colunas ou alterações frequentes em um schema relacional.

As consultas principais recuperam o histórico relacionado a um evento e ordenam as notificações pela data de criação. Esse padrão de documentos flexíveis e consultas por identificador faz sentido para um banco orientado a documentos.


## Tecnologias utilizadas

O projeto utiliza Java 21, Spring Boot 4.1.0, Maven, Spring Data JPA, PostgreSQL 17, MongoDB 8, Docker e Docker Compose.

## Portas utilizadas

| Componente | Porta |
|---|---:|
| API Gateway | 8080 |
| eventos-service | 8081 |
| notificacoes-service | 8082 |
| Discovery Server | 8761 |
| PostgreSQL no computador | 5433 |
| MongoDB | 27017 |

Internamente, o PostgreSQL continua utilizando a porta 5432 no container. A porta 5433 corresponde ao mapeamento realizado no computador.

## Como executar o projeto

Clone o repositório:

```shell
git clone https://github.com/Otavio-Oliveira94/gerenciamento-eventos-microservices.git
```

Acesse a pasta principal:

```shell
cd gerenciamento-eventos-microservices
```

Inicie os bancos:

```shell
docker compose up -d
```

Confirme que os containers estão saudáveis:

```shell
docker compose ps
```

Abra os projetos Maven no IntelliJ e execute as aplicações nesta ordem:

```text
1. DiscoveryServerApplication
2. EventosServiceApplication
3. NotificacoesServiceApplication
4. ApiGatewayApplication
```

Após a inicialização, os três componentes registrados deverão aparecer no dashboard do Eureka.

## Como interromper o projeto

Interrompa as aplicações pelo IntelliJ e depois execute:

```shell
docker compose stop
```

Para iniciá-las novamente:

```shell
docker compose start
```

Os volumes Docker mantêm os dados entre as execuções. O comando `docker compose down -v` não deve ser utilizado, exceto quando houver intenção de apagar os dados armazenados.

## Discovery Server

O dashboard do Eureka pode ser acessado em:

```text
http://localhost:8761
```

As aplicações esperadas são:

```text
API-GATEWAY
EVENTOS-SERVICE
NOTIFICACOES-SERVICE
```

O registro também pode ser consultado pelo Postman:

```text
GET http://localhost:8761/eureka/apps
Accept: application/json
```

## API Gateway

O API Gateway é o ponto único de entrada para chamadas externas da aplicação. Ele utiliza o Eureka para localizar dinamicamente as instâncias dos microserviços e encaminha as requisições através do Spring Cloud LoadBalancer.

O Gateway utiliza a porta 8080. As chamadas externas não precisam conhecer as portas internas dos microserviços.

### Rotas configuradas

| Rota externa | Serviço de destino |
|---|---|
| `/api/eventos/**` | eventos-service |
| `/api/notificacoes/**` | notificacoes-service |

### Como testar pelo API Gateway

Com todos os componentes em execução, as rotas podem ser testadas pelo Postman:

```text
GET http://localhost:8080/api/eventos
GET http://localhost:8080/api/notificacoes
```

As rotas configuradas podem ser consultadas em:

```text
GET http://localhost:8080/actuator/gateway/routes
```

As portas 8081 e 8082 permanecem disponíveis apenas para diagnóstico local.

## Endpoints do eventos-service

| Método | Endpoint pelo Gateway | Descrição |
|---|---|---|
| POST | `/api/eventos` | Cadastra um evento como rascunho |
| GET | `/api/eventos` | Lista os eventos |
| GET | `/api/eventos/{id}` | Consulta um evento |
| PUT | `/api/eventos/{id}` | Atualiza um evento em rascunho |
| DELETE | `/api/eventos/{id}` | Exclui um evento em rascunho |
| PATCH | `/api/eventos/{id}/publicar` | Publica um evento |
| PATCH | `/api/eventos/{id}/cancelar` | Cancela um evento publicado |

Um novo evento começa com o status `RASCUNHO`. As transições previstas são:

```text
RASCUNHO → PUBLICADO → CANCELADO
```

Somente eventos em rascunho podem ser atualizados ou excluídos.

### Exemplo de cadastro

```http
POST http://localhost:8080/api/eventos
Content-Type: application/json
```

```json
{
  "titulo": "Conferência de Arquitetura Distribuída",
  "subtitulo": "Microservices com Spring Cloud",
  "descricao": "Evento criado através do API Gateway.",
  "tipoEvento": "CONFERENCIA",
  "modalidade": "HIBRIDO",
  "dataHoraInicio": "2027-11-15T09:00:00",
  "dataHoraFim": "2027-11-15T17:00:00",
  "emailOrganizador": "organizador@email.com",
  "endereco": {
    "logradouro": "Av Paulista",
    "numero": "1578",
    "complemento": "Masp",
    "cep": "01310-200",
    "cidade": "São Paulo",
    "estado": "SP"
  }
}
```

## Endpoints do notificacoes-service

| Método | Endpoint pelo Gateway | Descrição |
|---|---|---|
| POST | `/api/notificacoes` | Cria e simula o envio de uma notificação |
| GET | `/api/notificacoes` | Lista o histórico |
| GET | `/api/notificacoes/{id}` | Consulta uma notificação |
| GET | `/api/notificacoes/evento/{eventoId}` | Lista notificações de um evento |

As notificações são mantidas como histórico. Por isso, o serviço não disponibiliza operações de edição ou exclusão.

## Comunicação entre microserviços

Quando um evento é publicado ou cancelado, o eventos-service chama o notificacoes-service por meio do OpenFeign. O endereço do serviço é obtido pelo Eureka e selecionado pelo Spring Cloud LoadBalancer.

A comunicação interna não passa pelo API Gateway. O Gateway recebe chamadas externas, enquanto os microservices comunicam-se diretamente usando descoberta de serviços.

## Estratégia de resiliência

A comunicação com o notificacoes-service é protegida por timeout, TimeLimiter, Circuit Breaker e fallback. O timeout de conexão do Feign é de dois segundos, o timeout de leitura é de três segundos e o TimeLimiter utiliza quatro segundos.

Se o notificacoes-service estiver indisponível, o fallback registra a falha no console e permite que a publicação ou o cancelamento seja concluído. A notificação é uma operação secundária e sua falha não deve impedir o gerenciamento do evento.

## Como demonstrar a resiliência

Crie um evento pelo Gateway e mantenha-o como rascunho. Em seguida, interrompa somente o notificacoes-service e publique o evento:

```text
PATCH http://localhost:8080/api/eventos/{id}/publicar
```

A resposta continuará apresentando `200 OK` e o evento ficará como `PUBLICADO`. O console do eventos-service registrará que a notificação falhou e que a operação principal foi mantida.

## Health checks

| Componente | Endereço |
|---|---|
| API Gateway | `http://localhost:8080/actuator/health` |
| eventos-service | `http://localhost:8081/actuator/health` |
| notificacoes-service | `http://localhost:8082/actuator/health` |
| Discovery Server | `http://localhost:8761/actuator/health` |

