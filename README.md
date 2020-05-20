# web-app-java
Inz-e-sys w javie

![Main page](/screenshots/1_main_page.png)


# 1. Załozenia projektu
<p align="justify">
Celem projektu jest utworzenie aplikacji bazodanowej majacej symulowac rozgrywki ligowe klubów
piłkarskich. Od strony zarzadzania rozgrywkami ma ona pomagac wpisywac terminarze i
rezultaty spotkan, edytowac informacje dotyczace piłkarzy i druzyn, w których graja. Bedzie
mozna pokazywac dane statystyczne, takie jak ilosc strzelonych bramek przez piłkarza.
Do aplikacji bedzie mozna zalogowac sie jako administrator lub zwykły uzytkownik. Zwykły
uzytkownik bedzie miec dostep do danych statystycznych i edycji informacji o sobie, administrator
bedzie mógł dodawac nowych uzytkowników oraz edytowac informacje odnosnie druzyn
i piłkarzy.
</p>

# 2. Wymagania
#### Wymagania projektowe minimalne:
* rejestracja nowych uzytkowników,
* logowanie,
* widok druzyn wraz z przypisanymi do nich piłkarzami,
* edycja danych uzytkowników.  

#### Wymagania rozszerzone:
* tabela ligowa ze zdobytymi punktami i bramkami,
* statystyki piłkarzy (np. strzelone bramki),
* edycja druzyn i piłkarzy przez adminów,
* terminarz spotkan.  

# 3. Technologie:
* frontend: HTML5, CSS, JavaScript, Thymeleaf, Three.js,
* backend: Java, Spring Boot, JPA,
* database: H2,
* authentication management: Keycloak.  
  
---  
* Table points
![2_points_table_sql_generated](/screenshots/2_points_table_sql_generated.png)
---
* End points examples
![3_end_points_examples](/screenshots/3_end_points_examples.png)
---
* Add new team page
![4_add_new_team_page](/screenshots/4_add_new_team_page.png)
---
* Keycloak login
![5_keycloak_login](/screenshots/5_keycloak_login.png)
---
* 3D-Photo
![6_3dphoto](/screenshots/6_3dphoto.png)
---
* Model stadium
![7_3dmodel_stadium](/screenshots/7_3dmodel_stadium.png)
---
* Keycloak profile settings
![8_keycloak_profile_settings](/screenshots/8_keycloak_profile_settings.png)
---
* H2 database console
![9_h2_database_console](/screenshots/9_h2_database_console.png)


