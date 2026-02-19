// package com.challangersop.challanger_sop.config;

// import com.challangersop.challanger_sop.entities.OrcamentoEntity;
// import com.challangersop.challanger_sop.enums.OrcamentoStatus;
// import com.challangersop.challanger_sop.enums.OrcamentoTipo;
// import com.challangersop.challanger_sop.repositories.OrcamentoRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Profile;
// import org.springframework.stereotype.Component;

// import java.math.BigDecimal;
// import java.time.LocalDate;

// @Component
// @Profile("dev")
// public class DevSeedRunner implements CommandLineRunner {

//     private final OrcamentoRepository orcamentoRepository;

//     public DevSeedRunner(OrcamentoRepository orcamentoRepository) {
//         this.orcamentoRepository = orcamentoRepository;
//     }

//     @Override
//     public void run(String... args) {
//         String protocolo = "43022.123456/2026-01";

//         boolean exists = orcamentoRepository.existsByNumeroProtocolo(protocolo);
//         if (exists) {
//             System.out.println("[DEV] Orçamento já existe. Pulando seed.");
//             return;
//         }

//         OrcamentoEntity orc = new OrcamentoEntity();
//         orc.setNumeroProtocolo(protocolo);
//         orc.setTipo(OrcamentoTipo.OBRA_EDIFICACAO);
//         orc.setValorTotal(new BigDecimal("1000.00"));
//         orc.setDataCriacao(LocalDate.now());
//         orc.setStatus(OrcamentoStatus.ABERTO);

//         OrcamentoEntity saved = orcamentoRepository.save(orc);
//         System.out.println("[DEV] Orçamento criado com id=" + saved.getId());
//     }
// }
