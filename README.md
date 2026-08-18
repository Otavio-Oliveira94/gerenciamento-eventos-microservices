# Gerenciamento de Eventos com Microservices

Este repositório contém um projeto acadêmico de gerenciamento de eventos desenvolvido com Java, Spring Boot e Maven. A aplicação será composta por microservices independentes, utilizando um API Gateway como ponto único de entrada e um Eureka Server para descoberta de serviços.

Atualmente, o eventos-service possui sua estrutura inicial de domínio e integração com PostgreSQL. O notificacoes-service possui sua estrutura Spring Boot inicial e posteriormente utilizará MongoDB para armazenar o histórico das notificações.

## Serviços

O eventos-service será responsável pelo cadastro, consulta, alteração, publicação e cancelamento dos eventos. Seu banco de dados será o PostgreSQL, utilizando o database lógico eventos_db.

O notificacoes-service será responsável pela criação e pelo armazenamento do histórico de notificações relacionadas aos eventos. Seu banco será o MongoDB, utilizando o database lógico notificacoes_db.

O discovery-server será responsável pelo registro e pela descoberta dinâmica dos serviços. O api-gateway será o ponto único de entrada para as chamadas externas.

## Tecnologias utilizadas

O projeto utiliza Java 21, Spring Boot 4.1.0, Maven, Spring Data JPA, PostgreSQL 17, MongoDB 8, Docker e Docker Compose.

## Portas

O eventos-service utiliza a porta 8081. O notificacoes-service utiliza a porta 8082. O PostgreSQL está disponível na porta 5433 do computador e o MongoDB na porta 27017.

O API Gateway utilizará futuramente a porta 8080 e o Discovery Server utilizará a porta 8761.

## Como executar atualmente

Primeiramente, os bancos de dados devem ser iniciados na pasta principal:

```shell
docker compose up -d
```

## Discovery Server

O projeto utiliza Netflix Eureka como servidor de descoberta. Os microservices registram seus nomes, endereços e portas no Discovery Server, permitindo que outros componentes localizem suas instâncias sem depender de endereços fixos.

O dashboard pode ser acessado em:

```text
http://localhost:8761
```