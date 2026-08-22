package cl.uchile.dcc.mobile.ecotienda.model

object DefaultData {
    val Product = listOf(
        Product(
            productName = "Cepillo",
            price = 5990,
            description = "Cepillo",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.etnic.es%2Fcdn%2Fshop%2Farticles%2Fcepillo_bambu_2.png%3Fv%3D1731865982&f=1&nofb=1&ipt=485207f662000c33f34a0fe43b5bbd2e3fe828d17de1d024c890fa6f36a71cef",
            stock = 5,
            category = "Cuidado Personal",
            producerID = "CL1"
        ),
        Product(
            productName = "Mochila",
            price = 10999,
            description = "Mochila",
            imageUrl = "https://images.pexels.com/photos/17870929/pexels-photo-17870929.jpeg",
            stock = 15,
            producerID = "AH1"
        ),
        Product(
            productName = "Esponja Natural",
            price = 4990,
            description = "Esponja muy Natural",
            imageUrl = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgTaxs3fGSKrwbE38m090MyGIkpNrKOhf0m1U2O59R5CkIW-pa00dSbBrUIeUUxllp3tvw8c9fDzMaOjgf1hPJYfyxUoDZsvtMCHYMCvdO_4hPnJ2jT3LtKtUaB2Im3busZYIXjrz6rCo7R/s900/natural-sea-sponge-skin-benefits.jpg",
            stock = 3,
            category = "Cuidado del Hogar",
            producerID = "MT1"
        ),

        
    )

    val Producer = listOf(
        Producer(
            id = "CL1",
            location = "Rancagua"
        ),
        Producer(
            id = "AH1",
        ),
        Producer(
            id = "MT1",
            location = "Concepcion"
        ),
        Producer(
            id = "TL1",
            location = "La Florida"
        ),
        Producer(
            id = "RC1",
            location = "Talca"
        ),
        Producer(
            id = "HV1",
            location = "La Serena"
        ),
        Producer(
            id = "EN1",
        ),
        Producer(
            id = "AC1",
        ),
    )
}