## Sobre o Projeto

Este projeto consiste na implementação do **backend** da aplicação de controle de **Orçamentos, Itens e Medições**, conforme especificação fornecida no desafio técnico.

A aplicação permite:

- Cadastro e edição de Orçamentos
- Cadastro e edição de Itens
- Cadastro de Medições
- Cadastro de Itens de Medição

---

## Arquitetura

O backend foi desenvolvido utilizando:

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Maven**

A arquitetura segue o padrão em camadas:

```

## Estrutura do Projeto

```
src/main/java/com/challangersop/challanger_sop/

├── controllers
├── services
├── repositories
├── entities
├── dtos
├── enums
├── exceptions
```

Separação clara entre:

- **Controller** → Camada de API
- **Service** → Regras de negócio
- **Repository** → Persistência
- **Entity** → Modelo de domínio
- **DTO** → Objetos de transferência
- **Exceptions** → Tratamento padronizado de erros

---

## Banco de Dados

### SGBD Utilizado

- **PostgreSQL**

---

### Script SQL Inicial

Arquivo: `database.sql` - (Onde será encontrado os comandos sqls)

---

## Configuração

### `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/challanger_sop
spring.datasource.username=challanger
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

---

## Como Executar

### Clonar repositório

```bash
git clone <repo>
cd backend
```

### Rodar aplicação

```bash
.\mvnw spring-boot:run
```

### Servidor disponível em:

```
http://localhost:8080

backchallengersop-production.up.railway.app
```

### Documentação swagger

https://backchallengersop-production.up.railway.app/swagger-ui/index.html#

---

## Endpoints Disponíveis

### Orçamentos

| Ação | Método | Endpoint |
|------|--------|----------|
| Criar | POST | `/orcamentos` |
| Buscar por ID | GET | `/orcamentos/{id}` |
| Listar | GET | `/orcamentos` |
| Finalizar | POST | `/orcamentos/{id}/finalizar` |

---

### Itens

| Ação | Método | Endpoint |
|------|--------|----------|
| Criar | POST | `/itens` |
| Listar por orçamento | GET | `/itens/orcamento/{orcamentoId}` |
| Atualizar | PUT | `/itens/{id}` |

---

### Medições

| Ação | Método | Endpoint |
|------|--------|----------|
| Criar | POST | `/medicoes` |
| Inserir item medido | POST | `/medicoes/{medicaoId}/itens` |
| Validar medição | POST | `/medicoes/{medicaoId}/validar` |

---

## Regras de Negócio Implementadas

### Orçamento

- Número do protocolo único
- Soma dos itens deve ser igual ao valor total para finalizar
- Não permite edição se estiver FINALIZADO

---

### Item

- Valor total calculado automaticamente
- Não permite inclusão/edição se orçamento estiver FINALIZADO
- Soma dos itens não pode ultrapassar o valor total do orçamento

---

### Medição

- Não pode existir mais de uma medição ABERTA por orçamento
- Status inicia como ABERTA
- Ao VALIDAR:
  - Atualiza quantidade acumulada do item
  - Não permite ultrapassar quantidade total do item
  - Calcula valor total da medição
- Não permite edição se estiver VALIDADA

---

## Tratamento de Erros

A aplicação utiliza exceções customizadas:

- `BusinessRuleException` → HTTP 422
- `NotFoundException` → HTTP 404

---

## Diagrama ER

O projeto inclui:

- Diagrama Entidade-Relacionamento
- Script SQL

![DER](https://i.postimg.cc/2yPsHDhn/DER.jpg)

---
