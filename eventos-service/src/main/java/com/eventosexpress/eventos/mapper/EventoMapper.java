package com.eventosexpress.eventos.mapper;

import com.eventosexpress.eventos.dto.request.EnderecoRequestDTO;
import com.eventosexpress.eventos.dto.request.EventoRequestDTO;
import com.eventosexpress.eventos.dto.response.EnderecoResponseDTO;
import com.eventosexpress.eventos.dto.response.EventoResponseDTO;
import com.eventosexpress.eventos.model.Endereco;
import com.eventosexpress.eventos.model.Evento;
import com.eventosexpress.eventos.model.enums.StatusEvento;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    private void copiarDados(
            EventoRequestDTO request,
            Evento evento
    ) {
        evento.setTitulo(request.titulo());
        evento.setSubtitulo(request.subtitulo());
        evento.setDescricao(request.descricao());
        evento.setTipoEvento(request.tipoEvento());
        evento.setModalidade(request.modalidade());
        evento.setDataHoraInicio(request.dataHoraInicio());
        evento.setDataHoraFim(request.dataHoraFim());
        evento.setEmailOrganizador(request.emailOrganizador());
        evento.setEndereco(toEndereco(request.endereco()));
    }

    private Endereco toEndereco(EnderecoRequestDTO enderecoRequest) {
        if (enderecoRequest == null) {
            return null;
        }

        Endereco endereco = new Endereco();

        endereco.setLogradouro(enderecoRequest.logradouro());
        endereco.setNumero(enderecoRequest.numero());
        endereco.setComplemento(enderecoRequest.complemento());
        endereco.setCep(enderecoRequest.cep());
        endereco.setCidade(enderecoRequest.cidade());
        endereco.setEstado(enderecoRequest.estado());

        return endereco;
    }

    private EnderecoResponseDTO toEnderecoResponse(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return new EnderecoResponseDTO(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCep(),
                endereco.getCidade(),
                endereco.getEstado()
        );
    }

    public Evento toEntity(EventoRequestDTO request) {
        Evento evento = new Evento();

        copiarDados(request, evento);
        evento.setStatus(StatusEvento.RASCUNHO);

        return evento;
    }

    public EventoResponseDTO toResponse(Evento evento) {
        return new EventoResponseDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getSubtitulo(),
                evento.getDescricao(),
                evento.getTipoEvento(),
                evento.getModalidade(),
                evento.getDataHoraInicio(),
                evento.getDataHoraFim(),
                evento.getEmailOrganizador(),
                evento.getStatus(),
                toEnderecoResponse(evento.getEndereco())
        );
    }

    public void updateEntity(
            EventoRequestDTO request,
            Evento evento
    ) {
        copiarDados(request, evento);
    }
}
