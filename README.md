Conews
======

Parser of a deleted aceboard forum on webarchive.org
Ce projet réalisé en Java (peu commenté..) a pour but de récupérer un forum sur webarchive.
Le forum CONews était hébergé sur Aceboard. Celui-ci a stoppé son service et les archives étaient inaccessibles. J'ai donc retrouvé lé forum sur WebArchive et lancé un script permettant de le crawler.
La routine est la suivante (après plusieurs possibilités) : arriver sur l'index, exporter le html, scanner tous les liens de la page, enregistrers les objets liées (css, images etc) et modifier leur chemin (car enregistrés en local)
