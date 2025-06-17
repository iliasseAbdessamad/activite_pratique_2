package me.iliasse.gestion_produits;

import me.iliasse.gestion_produits.entities.Product;
import me.iliasse.gestion_produits.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GestionProduitsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionProduitsApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ProductRepository productRepository){
		return (String... args) -> {
			try{
				productRepository.save(
						Product.builder()
								.name("Playstation 5")
								.price(2100)
								.image("")
								.quantity(23)
								.description("La PS5 est la console de nouvelle génération de Sony, conçue pour offrir des performances exceptionnelles et une immersion totale. Elle est équipée d’un processeur AMD Ryzen Zen 2 à 8 cœurs, d’un GPU RDNA 2, et d’un SSD ultra-rapide de 825 Go, ce qui permet des temps de chargement quasi instantanés et des graphismes en 4K fluides jusqu’à 120 images par seconde.\n" +
										"\n" +
										"La console dispose d’une manette révolutionnaire, la DualSense, avec des gâchettes adaptatives et un retour haptique immersif qui rendent chaque action plus réaliste. Elle est compatible avec de nombreux jeux PS4 et offre une bibliothèque en constante évolution de jeux exclusifs.\n" +
										"\n" +
										"Avec son design moderne, ses capacités audio 3D, et ses fonctionnalités avancées, la PS5 redéfinit la façon de jouer et promet une expérience de jeu plus rapide, plus fluide et plus intense.")
								.build());
				productRepository.save(
						Product.builder()
								.name("Xbox One")
								.price(1700)
								.quantity(30)
								.image("")
								.description("La Xbox One est une console de jeu développée par Microsoft, pensée pour offrir une expérience de divertissement complète. Elle est équipée d’un processeur AMD 8 cœurs, d’un GPU puissant, et propose une mémoire vive de 8 Go, assurant de bonnes performances pour le jeu et le multimédia.\n" +
										"\n" +
										"Compatible avec une large sélection de jeux, la Xbox One permet également de lire des films, écouter de la musique et accéder à des applications comme Netflix ou YouTube. Son interface fluide, sa manette ergonomique, et sa fonctionnalité de rétrocompatibilité avec les jeux Xbox 360 en font une console polyvalente.\n" +
										"\n" +
										"Idéale pour les joueurs comme pour les amateurs de contenu multimédia, la Xbox One combine puissance, confort et connectivité dans un seul appareil.")
								.build());
				productRepository.save(
						Product.builder()
								.name("Azus Gamer")
								.price(15000)
								.quantity(10)
								.image("")
								.description("Azur Gamer est une boutique spécialisée dans la vente de matériel et d’accessoires pour les passionnés de jeux vidéo. Elle propose une large gamme de consoles (PS5, Xbox, Nintendo Switch), de jeux vidéo récents et rétro, ainsi que de nombreux accessoires : manettes, casques, claviers gaming, souris, chaises ergonomiques et bien plus encore.\n" +
										"\n" +
										"Toujours à l’écoute des gamers, Azur Gamer mise sur des produits de qualité, des prix compétitifs et un service client attentionné. Que vous soyez un joueur occasionnel ou un compétiteur acharné, vous y trouverez tout ce qu’il faut pour améliorer votre expérience de jeu.\n" +
										"\n" +
										"Avec un espace convivial et une équipe passionnée, Azur Gamer vous accueille chaleureusement et vous accompagne dans chaque étape de votre aventure gaming.")
								.build());
				productRepository.save(
						Product.builder()
								.name("IPhone 12")
								.price(10000)
								.quantity(14)
								.image("")
								.description("L’iPhone 12 d’Apple allie performance et élégance. Il est équipé de la puce A14 Bionic, l’une des plus rapides du marché, offrant une fluidité remarquable pour toutes les tâches, du jeu aux applications pro.\n" +
										"\n" +
										"Son écran Super Retina XDR de 6,1 pouces affiche des couleurs éclatantes et un contraste exceptionnel. Compatible avec la 5G, il permet des téléchargements ultra-rapides et une navigation fluide.\n" +
										"\n" +
										"Côté photo, l’iPhone 12 dispose d’un double appareil photo 12 MP (grand-angle et ultra grand-angle), idéal pour capturer des images nettes de jour comme de nuit. Son design en aluminium et verre céramique ultra résistant lui donne un look moderne et une meilleure solidité.\n" +
										"\n" +
										"L’iPhone 12 est un choix idéal pour ceux qui recherchent un smartphone rapide, élégant et prêt pour le futur.")
								.build());
				productRepository.save(
						Product.builder()
								.name("Souris sans fil HP")
								.price(120)
								.quantity(50)
								.image("")
								.description("La souris sans fil HP est un accessoire fiable et pratique, idéal pour un usage quotidien à la maison, au bureau ou en déplacement. Grâce à sa connexion sans fil via récepteur USB ou Bluetooth (selon le modèle), elle offre une grande liberté de mouvement sans encombrement de câbles.\n" +
										"\n" +
										"Elle est conçue pour un confort optimal, avec une forme ergonomique adaptée aussi bien aux droitiers qu’aux gauchers, et une bonne prise en main pour un usage prolongé. Son capteur optique précis permet un déplacement fluide du curseur, que ce soit sur un bureau, un tapis ou une surface lisse.\n" +
										"\n" +
										"Discrète, légère et dotée d’une longue autonomie, la souris sans fil HP est parfaite pour ceux qui recherchent la performance sans sacrifier le confort.")
								.build());
				productRepository.save(
						Product.builder()
								.name("Casque Azus Gamer")
								.price(300)
								.quantity(33)
								.image("")
								.description("Le casque gamer Azus est un accessoire essentiel pour une expérience de jeu immersive. Doté de haut-parleurs haute définition, il offre un son stéréo ou surround puissant, idéal pour repérer chaque détail sonore : pas, tirs, ambiances... tout devient plus réel.\n" +
										"\n" +
										"Conçu pour le confort pendant les longues sessions de jeu, il dispose de coussinets rembourrés, d’un arceau réglable et parfois d’un design léger qui réduit la fatigue. Son microphone intégré, souvent ajustable ou amovible, garantit une communication claire avec les coéquipiers.\n" +
										"\n" +
										"Compatible avec PC, consoles (PS4, PS5, Xbox) et parfois même mobile, ce casque est un allié de choix pour les gamers exigeants. Certains modèles proposent aussi un éclairage LED pour un style encore plus gaming.\n" +
										"\n" +
										"Que vous jouiez en solo ou en multijoueur, le casque gamer vous plonge au cœur de l’action.")
								.build());
				productRepository.save(
						Product.builder()
								.name("Samsung Galaxy Watch")
								.price(420)
								.quantity(12)
								.image("")
								.description("La Samsung Galaxy Watch est bien plus qu’une montre : c’est un assistant intelligent qui vous accompagne partout. Elle permet de recevoir vos notifications, appels et messages directement à votre poignet, tout en gardant un œil sur votre santé et votre activité physique.\n" +
										"\n" +
										"Dotée d’un écran AMOLED lumineux, d’un design élégant et personnalisable, elle s’adapte aussi bien au sport qu’au quotidien. Elle intègre des capteurs avancés pour suivre votre rythme cardiaque, vos pas, votre sommeil, et même votre niveau de stress. Certains modèles offrent aussi un GPS intégré, la mesure d’oxygène dans le sang, ou encore un ECG.\n" +
										"\n" +
										"Compatible avec Android (et certains modèles avec iOS), la Galaxy Watch combine performance, autonomie solide et résistance à l’eau, le tout dans un format compact et moderne.\n" +
										"\n" +
										"Une montre parfaite pour ceux qui veulent allier style, technologie et bien-être.")
								.build());
				productRepository.save(
						Product.builder()
								.name("Vidéoprojecteur portable")
								.price(420)
								.quantity(12)
								.image("")
								.description("Le vidéoprojecteur portable transforme n’importe quel mur en écran géant, que ce soit pour un film, une présentation ou une session de jeu. Compact et facile à transporter, il est parfait pour les soirées cinéma à la maison, les déplacements professionnels ou les vacances.\n" +
										"\n" +
										"Il offre une image claire en HD ou Full HD, avec une projection allant jusqu’à 120 pouces selon le modèle et la distance. Grâce à ses connectivités variées (HDMI, USB, Bluetooth, Wi-Fi), il se connecte facilement à votre smartphone, ordinateur, console ou clé USB.\n" +
										"\n" +
										"Certains modèles sont équipés d’un haut-parleur intégré, d’une batterie rechargeable et même de systèmes Android embarqués, vous permettant de diffuser directement Netflix, YouTube ou d’autres applications sans appareil supplémentaire.\n" +
										"\n" +
										"Léger, pratique et performant, le vidéoprojecteur portable est l’outil idéal pour emporter le divertissement partout avec vous.")
								.build());
			}
			catch(Exception ex){
				System.out.println(ex.getClass().getCanonicalName());
				System.out.println(ex.getMessage());
			}
		};
	}
}