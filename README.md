# API RESTful EM SPRING BOOT #

## Introdução

Esse repositório foi criado com o objetivo de desenvolver uma API RESTful que permite o gerenciamento de caixas de e-mail, pastas e mensagens.

## Ferramentas Utilizadas:

- Spring Data JPA

   - O Spring Data tem como objetivo simplificar o trabalho com a persistência de dados, reduzindo o tempo necessário para configurar o JPA. Isso, por sua vez, aumenta a produtividade nas camadas de persistência de dados.
  
- Spring Boot Devtools

   - O DevTools foi selecionado por configurar automaticamente algumas propriedades úteis durante o desenvolvimento, além de monitorar o classpath para reiniciar automaticamente a aplicação sempre que houver alterações.
   
- MySQL
   - O MySQL foi escolhido por ser um sistema de gerenciamento de banco de dados amplamente utilizado, oferecendo robustez, desempenho e escalabilidade.
   - Sua facilidade de integração com o Spring Data JPA, juntamente com o suporte a transações e a capacidade de manipular grandes volumes de dados de forma eficiente, foram fatores decisivos para a escolha.
- Lombok
   - O Lombok foi selecionado por sua capacidade de reduzir a verbosidade do código, gerando automaticamente métodos como getters, setters, construtores e outros, a partir de anotações simples.
   - Isso economiza tempo e melhora a legibilidade do código, permitindo que os desenvolvedores se concentrem mais na lógica de negócios ao invés de escrever código repetitivo.    

## Ferramentas adicionais:

- Para realizar as requisições em formato JSON, foi utilizado o Postman, uma ferramenta popular para testes de APIs. [Postman](https://www.getpostman.com/apps).

## Montagem do ambiente:

- Código disponível para cloanagem:

   > https://github.com/jeanlucasbs/MailBoxManager
  
- Logo após baixe o Eclipse IDE for Java EE Developers neste link (https://www.eclipse.org/downloads/packages/release/kepler/r/eclipse-ide-java-ee-developers) e instale-o normalmente no seu computador.
- Importe o projeto Maven.


## Como Executar o projeto:

- Abra a classe *MailBoxManager*, localizada no caminho: _src/main/java/MailBoxManagerApplication.
- Execute o código através do menu: run > run as > Java Application

## Uso dos serviços:

1) Com o Postman execute os seguintes endpoints abaixo:

- *POST* 
   - `http://localhost:8080/api/v1/mailboxes` - Cria uma caixa de e-mail.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/folders` - Criação de pasta associada a uma caixa.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/send-message ` - Salva o envio de uma mensagem de uma caixa.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/receive-message` - Salva o recebimento de uma mensagem em uma caixa.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read` - Altera os status de uma mensagem de lida ou não lida.
  
- *GET* 
   - `http://localhost:8080/api/v1/mailboxes` - Lista as caixas armazenadas.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/folders` - Lista as pastas de uma caixa.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages` - Lista as mensagens de uma caixa e pasta.
   - `http://localhost:8080/api/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}` - Visualizar os detalhes de uma mensagem.

- *Versão V2 os itens 6,7,8*
   - Foram adicionadas 3 endpoints para trabalhar com paginação. Este mecanismo permite dividir grandes volumes de dados em partes menores e mais gerenciáveis, facilitando o processamento e a exibição.
   - Isso melhora o desempenho da aplicação e a experiência do usuário ao fornecer apenas uma parte dos resultados por vez, geralmente com parâmetros como número da página e tamanho da página.
   
2) No repositório do projeto há um arquivo chamado *test-uol.postman_collection.json* que pode ser utilizado para importar no Postaman com todos os endpoints configurados.
     
   
## Containirização do projeto
  
Dockerfile

  - O Dockerfile foi utilizado para criar a imagem Docker da aplicação. Ele define o ambiente de execução necessário, como o sistema operacional base, as dependências e as etapas de compilação.
  - Isso garante que a aplicação seja executada de maneira consistente em qualquer ambiente que suporte Docker.
  - No repositório do projeto há o arquivo *Dockerfile*.

Docker Compose

  - O Docker Compose foi empregado para orquestrar múltiplos contêineres em um ambiente de desenvolvimento ou produção. Foram criados um container da aplicação e outro do banco de dados.
  - No repositório do projeto há o arquivo *docker-compose.yml*.
    
