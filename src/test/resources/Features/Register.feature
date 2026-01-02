Feature: Registrering

  Background:
    Given Jag är på registreringssidan


  Scenario: Lyckad registrering
    When Jag fyller i alla fält korrekt
    And Jag klickar på Join-knappen
    Then Jag ska få ett meddelande om att kontot har skapats

    Scenario: Skapa användare - efternamn saknas
      When Jag fyller i alla fält men lämnar efternamn tomt
      And Jag klickar på Join-knappen
      Then Jag ska se ett felmeddelande om att efternamn krävs


      Scenario: Skapa användare - lösenord matchar inte
        When Jag fyller i alla fält men skriver olika lösenord
        And Jag klickar på Join-knappen
        Then Jag ska se ett felmeddelande om att lösenorden inte matchar

        Scenario: Skapa användare - Terms and Conditions ej godkänt
          When Jag fyller i alla fält men kryssar inte i Terms and Conditions
          And Jag klickar på Join-knappen
          Then Jag ska se ett felmeddelande om att villkoren måste godkännas

          Scenario Outline: Misslyckad registrering med olika felaktiga lösenord
            When Jag fyller i alla fält men med lösenord "<password>" och bekräftelse "<confirm>"
            And Jag klickar på Join-knappen
            Then Jag ska se ett felmeddelande om att lösenorden inte matchar

            Examples:
            | password     | confirm |
            | password000! | password777! |
            | tigermannen  | tigerkvinnan |
            | volvo123     | volvo321     |
            | pokemon23    | pokebowl23   |

            Scenario Outline: Registrering på olika webbläsare

              Given Jag startar webbläsaren "<Browser>"
              And Jag är på registreringssidan
              When Jag fyller i alla fält korrekt
              And Jag klickar på Join-knappen
              Then Jag ska få ett meddelande om att kontot har skapats

              Examples:
              | Browser |
              | Chrome  |
              | Edge    |