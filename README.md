# 📚 Apache Camel - Anotações de Estudo

## 🧠 Objetivo
Este documento reúne conceitos aprendidos durante o estudo de Apache Camel, além de dúvidas e pontos para revisão futura.

---

# 🚀 Conceitos Fundamentais do Apache Camel

## 🔁 Rotas (Routes)
- Representam o fluxo de dados dentro da aplicação
- Definidas usando `RouteBuilder`
- Estrutura básica:
```java
from("origem")
    .to("destino");
```

---

## 🔗 Endpoints
- Pontos de entrada ou saída de dados
- Sempre definidos como strings
- Exemplos:
  - `timer:first-timer`
  - `log:first-timer`
  - `file:files/input`
  - `activemq:my-queue`

---

## 🧩 Componentes
- São os “tipos” de endpoints
- Determinam como o Camel se comunica com o mundo externo

Exemplos:
- `timer` → gera eventos periódicos
- `log` → escreve logs
- `file` → lê/escreve arquivos
- `activemq` → integração com mensageria

---

## 🔄 Transformação (Transformation)
- Modifica o conteúdo da mensagem (body)

Exemplo com bean:
```java
.bean("getCurrentTimeBean")
```

- O retorno do método substitui o corpo da mensagem

---

## 📦 Exchange
- Representa a mensagem que trafega na rota
- Contém:
  - Body (conteúdo)
  - Headers (metadados)

---

## 🔀 Padrão de Mensagem
- `InOnly` → envio sem resposta (fire-and-forget)

---

# ⏱ Timer

## 🔹 Comportamento padrão
- Executa a cada **1 segundo**

## 🔹 Uso básico
```java
from("timer:first-timer")
    .to("log:first-timer");
```

---

# 🧠 Beans no Camel

## 🔹 Uso
```java
.bean("getCurrentTimeBean")
```

## 🔹 Funcionamento
- Camel executa automaticamente o método do bean
- O retorno vira o novo body da mensagem

---

# 📝 Logging

## 🔹 Forma correta
```java
.log("${body}")
```

---

# 📂 File Component

## 🔹 Exemplo
```java
from("file:files/input")
    .to("file:files/output");
```

## 🔹 Comportamento
- Move arquivos automaticamente
- Cria pasta `.camel` dentro do input

---

# 📨 ActiveMQ (Mensageria)

## 🔹 Execução via container Docker

- docker run -p 61616:61616 -p 8161:8161 docker.io/apache/activemq-classic

## 🔹 Portas
- `61616` → comunicação de mensagens
- `8161` → console administrativa

## 🔹 Producer
```java
.to("activemq:my-activemq-queue")
```

## 🔹 Consumer
```java
from("activemq:my-activemq-queue")
    .to("log:received-message-from-activemq");
```

---

# 🧠 Arquitetura atual

```
Microservice A → ActiveMQ → Microservice B
```

---

# ⚠️ Problemas e Aprendizados

## 🔹 YAML
- Espaço após `:` é obrigatório

## 🔹 IntelliJ
- Pode exigir **Reload Project**

---

# ❓ Dúvidas para Revisar

- Diferença entre `activemq:` e `jms:`
- Como funciona retry no Camel
- Tratamento de erro

---

# 💡 Insights

- Camel é baseado em fluxo de mensagens
- Mensageria permite desacoplamento entre sistemas

---

# 🚀 Próximos Passos

- Estudar paralelismo
- Explorar mais componentes do Camel
