package br.com.liebersonsantos.masteringrecyclerview.model

data class Email(
    val user: String,
    val subject: String,
    val preview: String,
    val date: String,
    val stared: Boolean,
    val unread: Boolean,
    var selected: Boolean = false
)

class EmailBuilder {
    var user: String = ""
    var subject: String = ""
    var preview: String = ""
    var date: String = ""
    var stared: Boolean = false
    var unread: Boolean = false

    fun build(): Email = Email(user, subject, preview, date, stared, unread, selected = false)
}

fun email(block: EmailBuilder.() -> Unit): Email = EmailBuilder().apply(block).build()

fun fakeEmails() = mutableListOf(
    email {
        user = "Facebook"
        subject = "veja nossas três dicas principais"
        preview = "Olá..."
        date = "20 Nov"
        stared = true
    },
    email {
        user = "Facebook"
        subject = "Um amigo quer que você curta uma página"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "YouTube"
        subject = "Você acaba de receber um vídeo"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "Instagram"
        subject = "veja nossas três dicas principais"
        preview = "Olá..."
        date = "20 Nov"
        stared = true
    },
    email {
        user = "Facebook"
        subject = "veja nossas três dicas principais"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "Facebook"
        subject = "Um amigo quer que você curta uma página"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "YouTube"
        subject = "Você acaba de receber um vídeo"
        preview = "Olá..."
        date = "20 Nov"
        stared = true
    },
    email {
        user = "Instagram"
        subject = "veja nossas três dicas principais"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "Facebook"
        subject = "veja nossas três dicas principais"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "Facebook"
        subject = "Um amigo quer que você curta uma página"
        preview = "Olá..."
        date = "20 Nov"
        stared = false
    },
    email {
        user = "YouTube"
        subject = "Você acaba de receber um vídeo"
        preview = "Olá..."
        date = "20 Nov"
        stared = true
    },
    email {
        user = "Instagram"
        subject = "veja nossas três dicas principais"
        preview = "Olá..."
        date = "20 Nov"
        stared = true
    }
)