package cl.uchile.dcc.mobile.ecotienda.model

// Datos usados para primera iteración de la app
object DefaultData {
    val Product = listOf(
        Product(
            productName = "Cepillo",
            price = 5990,
            description = "Cepillo",
            productProducer = "Danitza Jara",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.etnic.es%2Fcdn%2Fshop%2Farticles%2Fcepillo_bambu_2.png%3Fv%3D1731865982&f=1&nofb=1&ipt=485207f662000c33f34a0fe43b5bbd2e3fe828d17de1d024c890fa6f36a71cef",
            stock = 5,
            category = "Cuidado Personal",
            producerID = "CL1"
        ),
        Product(
            productName = "Mochila",
            price = 10999,
            description = "Mochila",
            productProducer = "Ignacio Salinas",
            imageUrl = "https://images.pexels.com/photos/17870929/pexels-photo-17870929.jpeg",
            stock = 15,
            category = "Moda Natural",
            producerID = "AH1"
        ),
        Product(
            productName = "Esponja Natural",
            price = 4990,
            description = "Esponja muy Natural",
            productProducer = "Cristina Ortiz",
            imageUrl = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgTaxs3fGSKrwbE38m090MyGIkpNrKOhf0m1U2O59R5CkIW-pa00dSbBrUIeUUxllp3tvw8c9fDzMaOjgf1hPJYfyxUoDZsvtMCHYMCvdO_4hPnJ2jT3LtKtUaB2Im3busZYIXjrz6rCo7R/s900/natural-sea-sponge-skin-benefits.jpg",
            stock = 3,
            category = "Cuidado del Hogar",
            producerID = "MT1"
        ),
        Product(
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
            productName = "Hueso",
            price = 4990,
            description = "Producto Sustentable",
            productProducer = "Javiera Montenegro",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.explicit.bing.net%2Fth%2Fid%2FOIP.sySMph4VYgoi6_wSvyghgwHaGL%3Fr%3D0%26pid%3DApi&f=1&ipt=243fee6e4d3c8ef085eff927222645ca1a3135809283fc203dabd612d4904474&ipo=images",
            stock = 3,
            category = "Mascotas",
            producerID = "MT1"
        ),



        )

    val Producer = listOf(
        Producer(
            id = "CL1",
            name = "Mónica Cisternas",
            description = "Fundadora de Raíz Textil",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Transforma excedentes de algodón en prendas cómodas y atemporales, confeccionadas junto a talleres de Santiago.",
            location = "Rancagua"
        ),
        Producer(
            id = "AH1",
            name = "Javiera Montenegro",
            description = "Creador de Oficio Sur",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Diseña objetos durables para el hogar usando maderas recuperadas y técnicas tradicionales del sur de Chile.",
            location = "Concepcion"
        ),
        Producer(
            id = "MT1",
            name = "Isidora Regno",
            description = "Diseñadora de Bruma",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Crea colecciones de moda consciente en series pequeñas, privilegiando fibras naturales y trazabilidad local.",
            location = "Concepcion"
        ),
        Producer(
            id = "TL1",
            name = "Ignacio Salinas",
            description = "Fundador de Circular Lab",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Desarrolla accesorios a partir de materiales descartados, combinando innovación circular y comercio justo.",
            location = "La Florida"
        ),
        Producer(
            id = "RC1",
            name = "Danitza Jara",
            description = "Fundadora de Botánica",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Formula cuidado personal de origen vegetal, sin tóxicos y con envases retornables producidos localmente.",
            location = "Talca"
        ),
        Producer(
            id = "HV1",
            name = "Carlos Ortiz",
            description = "Creador de Taller Nativo",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Convierte fibras y maderas recuperadas en objetos cotidianos de larga duración y bajo impacto ambiental.",
            location = "La Serena"
        ),
        Producer(
            id = "EN1",
            name = "Cristina Ortiz",
            description = "Fundadora de Verde Hogar",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Crea soluciones de limpieza biodegradables con ingredientes locales y sistemas de recarga reutilizables.",
            location = "Valdivia"
        ),
        Producer(
            id = "AC1",
            name = "Luis Pereira",
            description = "Creador de Norte Vivo",
            imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.exPxGSNU454QZLM7QpeWzgHaHa%3Fr%3D0%26pid%3DApi&f=1&ipt=725341a5c8a022ba61342d37bfb3495eb9560f3e2535476cc77095865b029428&ipo=images",
            history = "Elabora accesorios funcionales con descartes textiles, generando oportunidades de trabajo en su comunidad.",
            location = "Coquimbo"
        ),
    )
}