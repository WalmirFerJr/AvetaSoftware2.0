## WalletWise - Gestor de Carteira

API em Spring Boot para gestão de carteira de investimentos, permitindo cadastrar aportes em **Renda Variável** (Ações/FIIs) e **Renda Fixa** (CDB/CDI), calcular o **saldo consolidado** e consultar o **extrato de operações**.

### Stack técnica

- **Java**: 21
- **Spring Boot**: 3.3.x
- **Maven**
- **PostgreSQL** (via Docker)
- **Spring Web, Spring Data JPA, Validation, Lombok, Spring Boot DevTools**
- **SpringDoc OpenAPI** (Swagger UI)

### Arquitetura de pastas

`src/main/java/br/usp/walmir/walletwise/`

- `controller/` – Endpoints REST (`UserController`, `AssetController`, `FixedIncomeController`, `PortfolioController`, `TransactionController`)
- `service/` – Regras de negócio e cálculos (`UserService`, `AssetService`, `FixedIncomeService`, `PortfolioService`, `TransactionService`)
- `repository/` – Interfaces JPA para o PostgreSQL
- `model/` – Entidades de banco (`User`, `Portfolio`, `Asset`, `FixedIncome`, `Transaction`, enums)
- `dto/` – DTOs de request/response com Bean Validation
- `exception/` – Exceções customizadas e `GlobalExceptionHandler`
- `config/` – Configuração do Swagger/OpenAPI

### Modelagem de domínio (resumo)

- **User**
  - Campos: `id`, `name`, `email` (único), `createdAt`
  - Relacionamento: `1-1` com `Portfolio`
  - Regra: ao criar um usuário, é criada automaticamente uma carteira `"Carteira Padrão"`.

- **Portfolio**
  - Campos: `id`, `name`, `createdAt`
  - Relacionamentos:
    - `1-1` com `User`
    - `1-N` com `Asset`
    - `1-N` com `FixedIncome`
    - `1-N` com `Transaction`

- **Asset** (Renda Variável)
  - Campos: `id`, `ticker`, `quantity`, `averagePrice`, `createdAt`, `updatedAt`
  - Relacionamento: `N-1` com `Portfolio`
  - Regra: representa a posição de um ativo de bolsa (ações/FIIs).

- **FixedIncome** (Renda Fixa)
  - Campos: `id`, `name`, `principal`, `monthlyRate`, `startDate`, `maturityDate`, `createdAt`
  - Relacionamento: `N-1` com `Portfolio`
  - Regra: principal é o valor aplicado; `monthlyRate` é a taxa mensal em decimal (ex: `0.01` = 1%/mês).

- **Transaction**
  - Campos: `id`, `type (BUY/SELL/DEPOSIT/WITHDRAW)`, `assetType (VARIABLE_INCOME/FIXED_INCOME)`,
    `assetTicker`, `quantity`, `amount`, `transactionDate`
  - Relacionamento: `N-1` com `Portfolio`
  - Regra: loga toda operação de compra de ações/FIIs e aportes em renda fixa.

### Requisitos

- **Cadastro de Ativos**
  - `POST /assets`
    - Request: `AssetRequestDto` com `portfolioId`, `ticker`, `quantity`, `price`.
    - Validação: `@NotBlank` para `ticker`, `@Positive` para `quantity` e `price`.
    - Ação: cria um `Asset` associado a uma `Portfolio` e registra uma `Transaction` do tipo `BUY`.

- **Saldo da Carteira**
  - `GET /portfolios/{id}/summary`
    - Retorna `PortfolioSummaryDto` com:
      - `totalVariableIncome` = soma de `quantity * averagePrice` de todos os `Asset`.
      - `totalFixedIncome` = soma de `principal` de todos os `FixedIncome`.
      - `totalInvested` = soma dos dois.

- **Simulação de Rendimento (Renda Fixa)**
  - Fórmula: \\(M = P (1 + i)^n\\)
  - Implementada em `FixedIncomeService.simulateCompoundInterest`.
  - Endpoint: `GET /fixed-incomes/{id}/simulation?months=X`
    - Retorna `FixedIncomeSimulationDto` com `principal`, `monthlyRate`, `months`, `projectedAmount`.

- **Extrato**
  - `GET /portfolios/{portfolioId}/transactions`
    - Response: lista de `TransactionResponseDto`.
    - Filtros opcionais: `from` e `to` (datas no padrão ISO `yyyy-MM-dd'T'HH:mm:ss`).

### Endpoints principais

- **Usuário e Carteira**
  - `POST /users` – cria usuário e carteira padrão.
  - `GET /users/{id}` – detalhes do usuário.
  - `GET /users/{id}/portfolio` – carteira do usuário.
  - `GET /portfolios/{id}/summary` – resumo de patrimônio.

- **Renda Variável (Ações/FIIs)**
  - `POST /assets`
  - `GET /assets/portfolio/{portfolioId}`

- **Renda Fixa (CDB/CDI)**
  - `POST /fixed-incomes`
  - `GET /fixed-incomes/portfolio/{portfolioId}`
  - `GET /fixed-incomes/{id}/simulation?months=X`

- **Extrato**
  - `GET /portfolios/{portfolioId}/transactions?from=&to=`

### Validação e tratamento de erros

- DTOs de entrada usam Bean Validation:
  - `@NotBlank`, `@NotNull`, `@Positive`, `@Email`, etc.
- `GlobalExceptionHandler` com:
  - `ResourceNotFoundException` → HTTP 404.
  - `MethodArgumentNotValidException`, `ConstraintViolationException`, `IllegalArgumentException` → HTTP 400.
  - Demais exceções → HTTP 500 com mensagem genérica.
- Response de erro padrão `ApiError`:
  - Campos: `timestamp`, `status`, `error`, `message`, `path`, `fieldErrors`.

### Swagger / OpenAPI

- Dependência: `springdoc-openapi-starter-webmvc-ui`.
- Configuração: `OpenApiConfig` com `@OpenAPIDefinition`.
- Acesse a documentação interativa em:
  - `http://localhost:8080/swagger-ui/index.html`

```ts
const api = axios.create({
  baseURL: "http://localhost:8080",
});
```

e fazer chamadas como:

```ts
await api.post("/users", { name, email });
await api.post("/assets", { portfolioId, ticker, quantity, price });
await api.get(`/portfolios/${portfolioId}/summary`);
```

### Como executar o projeto

**Pré-requisitos**

- Java 21 instalado
- Maven instalado
- Docker + Docker Compose instalados

**Passos**

1. Subir o PostgreSQL com Docker:

   ```bash
   docker-compose up -d
   ```

   O banco será criado com:

   - DB: `walletwise`
   - User: `walletwise`
   - Senha: `walletwise`

2. Rodar a aplicação Spring Boot:

   ```bash
   mvn spring-boot:run
   ```
    - Ou rodar na sua IDE com o run
    - Atenção à porta (verifique se o projeto está sendo inicializado no ipv4 ou ipv6)

3. Acessar a API:

   - Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Regras de negócio (resumo)

- Todo **User** criado recebe automaticamente uma **Portfolio** padrão.
- A soma do patrimônio considera:
  - Renda variável pelo **custo de aquisição** (quantidade × preço médio).
  - Renda fixa pelo **principal aplicado**.
- Toda compra de ativo (`POST /assets`) gera uma **Transaction** `BUY`.
- Todo aporte em renda fixa (`POST /fixed-incomes`) gera uma **Transaction** `DEPOSIT`.
- A simulação de juros compostos considera:
  - Taxa mensal em decimal (`monthlyRate`).
  - Número de meses informado no endpoint.

