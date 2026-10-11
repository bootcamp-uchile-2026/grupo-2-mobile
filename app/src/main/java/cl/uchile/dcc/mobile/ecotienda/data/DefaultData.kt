package cl.uchile.dcc.mobile.ecotienda.data

// Datos usados para primera iteración de la app
object DefaultData {
    val Product = listOf(
        Product(
            id = "PRD-CEPILLO",
            productName = "Cepillo",
            price = 5990,
            description = "Cepillo",
            productProducer = "CampoLindo",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.etnic.es%2Fcdn%2Fshop%2Farticles%2Fcepillo_bambu_2.png%3Fv%3D1731865982&f=1&nofb=1&ipt=485207f662000c33f34a0fe43b5bbd2e3fe828d17de1d024c890fa6f36a71cef",
            stock = 5,
            category = "Cuidado Personal",
            producerID = "CL1"
        ),
        Product(
            id = "PRD-MOCHILA",
            productName = "Mochila",
            price = 10999,
            description = "Mochila",
            productProducer = "Agua Hermosa",
            imageUrl = "https://images.pexels.com/photos/17870929/pexels-photo-17870929.jpeg",
            stock = 15,
            category = "Moda Natural",
            producerID = "AH1"
        ),
        Product(
            id = "PRD-ESPONJA",
            productName = "Esponja Natural",
            price = 4990,
            description = "Esponja muy Natural",
            productProducer = "Maritima",
            imageUrl = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgTaxs3fGSKrwbE38m090MyGIkpNrKOhf0m1U2O59R5CkIW-pa00dSbBrUIeUUxllp3tvw8c9fDzMaOjgf1hPJYfyxUoDZsvtMCHYMCvdO_4hPnJ2jT3LtKtUaB2Im3busZYIXjrz6rCo7R/s900/natural-sea-sponge-skin-benefits.jpg",
            stock = 3,
            category = "Cuidado del Hogar",
            producerID = "MT1"
        ),
<<<<<<< HEAD:app/src/main/java/cl/uchile/dcc/mobile/ecotienda/model/DefaultData.kt
=======
        Product(
            id = "PRD-POLERA",
            productName = "Polera Aurora",
            price = 4990,
            description = "Producto Sustentable",
            productProducer = "Luis Pereira",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.3Y-GiZiW8JptBlHORnKV4gHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=facb6669e2a63c9cf0a46b6ffb1da30bbf2681f40c68ce7c2f3386d7b61addd8&ipo=images",
            stock = 3,
            category = "Moda Natural",
            producerID = "MT1"
        ),
        Product(
            id = "PRD-HUESO",
            productName = "Hueso",
            price = 4990,
            description = "Producto Sustentable",
            productProducer = "Javiera Montenegro",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.explicit.bing.net%2Fth%2Fid%2FOIP.sySMph4VYgoi6_wSvyghgwHaGL%3Fr%3D0%26pid%3DApi&f=1&ipt=243fee6e4d3c8ef085eff927222645ca1a3135809283fc203dabd612d4904474&ipo=images",
            stock = 3,
            category = "Mascotas",
            producerID = "MT1"
        ),
>>>>>>> feature/data-sqlite:app/src/main/java/cl/uchile/dcc/mobile/ecotienda/data/DefaultData.kt

        
    )

    val Producer = listOf(
        Producer(
            id = "CL1",
            name = "Campo Lindo",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Campo Lindo",
            location = "Rancagua"
        ),
        Producer(
            id = "AH1",
            name = "Agua Hermosa",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Agua Hermosa",
            location = "Santiago"
        ),
        Producer(
            id = "MT1",
            name = "Maritima",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Maritima",
            location = "Concepcion"
        ),
        Producer(
            id = "TL1",
            name = "Taller Luna",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Taller Luna",
            location = "La Florida"
        ),
        Producer(
            id = "RC1",
            name = "Raices Co.",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Raices Co.",
            location = "Talca"
        ),
        Producer(
            id = "HV1",
            name = "Hilos vivo",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Hilos vivo",
            location = "La Serena"
        ),
        Producer(
            id = "EN1",
            name = "Extracto Natural",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Extracto Natural",
            location = "Coquimbo"
        ),
        Producer(
            id = "AC1",
            name = "Ancestros",
            description = "",
            imageUrl = "https://www.explorationjunkie.com/chile-national-animal/",
            history = "Ancestros",
            location = "Valdivia"
        ),
    )
}