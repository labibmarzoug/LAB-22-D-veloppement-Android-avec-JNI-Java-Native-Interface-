# Rapport de Laboratoire : Android JNI / NDK — Projet JNIDemo

## 1. Introduction

Ce laboratoire a pour objectif de développer une application Android nommée JNIDemo permettant de communiquer entre du code Java et du code natif C++ via JNI (Java Native Interface).

L’intégration de code natif permet d’améliorer les performances, de sécuriser certaines parties sensibles du code et de réutiliser des bibliothèques C/C++ existantes.

---

## 2. Objectifs

À la fin de ce laboratoire, il est possible de :

- Créer un projet Android avec support C++
- Comprendre le rôle du NDK, de CMake et de JNI
- Déclarer et appeler des méthodes natives
- Manipuler des types entre Java et C++
- Gérer les erreurs comme UnsatisfiedLinkError
- Lire les logs natifs dans Logcat
- Appliquer les bonnes pratiques JNI

---

## 3. Prérequis

Les éléments suivants sont nécessaires :

- Android Studio installé
- SDK Android configuré
- NDK, CMake et LLDB installés via SDK Manager
- Connaissances de base en Java et Android

---

## 4. Création du projet

Étapes :

- Ouvrir Android Studio
- Créer un nouveau projet
- Choisir le template "Native C++"
- Configurer le projet (nom : JNIDemo, langage : Java)

Android Studio génère automatiquement les fichiers nécessaires :
- Code Java
- Code C++
- Fichier CMakeLists.txt
  
<img width="1670" height="755" alt="image" src="https://github.com/user-attachments/assets/4e585d8b-4883-4681-8d5c-446926a800b1" />

---
<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/7ef81716-622b-4690-9eb1-58f862db3bfd" />

---

## 5. Configuration CMake

Fichier : app/src/main/cpp/CMakeLists.txt

```cmake
cmake_minimum_required(VERSION 3.22.1)

project("jnidemo")

add_library(
        native-lib
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        ${log-lib})
```

Explication :

- cmake_minimum_required : définit la version minimale de CMake
- project : définit le nom du projet
- add_library : crée une bibliothèque native (.so)
- find_library : récupère la bibliothèque système log
- target_link_libraries : lie les bibliothèques entre elles
  
<img width="1882" height="1030" alt="image" src="https://github.com/user-attachments/assets/e2ce3df5-9571-4a7c-9031-d9a993f67718" />

---

<img width="1919" height="1041" alt="image" src="https://github.com/user-attachments/assets/9486005b-26c7-4f0b-b77a-b3fdea35b4ba" />

 ---

## 6. Implémentation du code natif C++

Fichier : native-lib.cpp

Fonctionnalités implémentées :

1. Fonction helloFromJNI
   Retourne une chaîne de caractères depuis C++ vers Java.

2. Fonction factorial
   Calcule le factoriel d’un entier.
   Gestion des erreurs :
   - n négatif : retourne -1
   - dépassement de capacité : retourne -2

3. Fonction reverseString
   - Reçoit une chaîne Java
   - La convertit en C++
   - Inverse la chaîne
   - Retourne le résultat vers Java

4. Fonction sumArray
   - Reçoit un tableau d’entiers
   - Calcule la somme
   - Gère les erreurs (tableau nul, overflow)

---

<img width="1918" height="1079" alt="image" src="https://github.com/user-attachments/assets/5ef49c1b-65db-4351-a2d9-8e204a6028f4" />

---

## 7. Déclaration des méthodes natives en Java

Fichier : MainActivity.java

```java
public native String helloFromJNI();
public native int factorial(int n);
public native String reverseString(String s);
public native int sumArray(int[] values);
```

Le mot-clé native indique que les méthodes sont implémentées en C++.

<img width="1918" height="1079" alt="image" src="https://github.com/user-attachments/assets/1a1b0bc6-6280-49ff-8359-2f9a8f0bc2c3" />


---

## 8. Chargement de la bibliothèque native

```java
static {
    System.loadLibrary("native-lib");
}
```
<img width="1907" height="1041" alt="image" src="https://github.com/user-attachments/assets/8887d50f-c0ba-4935-a1f4-42d0c6846b02" />


Cette étape est obligatoire pour charger la bibliothèque native au démarrage de l’application.

---

## 9. Appel des fonctions natives

Dans la méthode onCreate :

- Appel de helloFromJNI
- Calcul du factoriel
- Inversion d’une chaîne
- Somme d’un tableau

Les résultats sont affichés dans des TextView.

---

## 10. Interface utilisateur

Fichier : activity_main.xml

Structure :

- ScrollView pour permettre le défilement
- LinearLayout vertical
- Quatre TextView pour afficher les résultats

---

## 11. Compilation et exécution

Lancer l’application sur un émulateur ou un appareil réel.

Résultat attendu :

Hello from C++ via JNI !  
Factoriel de 10 = 3628800  
Texte inverse : !lufrewop si INJ  
Somme du tableau = 150  

---

<img width="1880" height="726" alt="image" src="https://github.com/user-attachments/assets/15ca8b31-4185-4d8d-9d92-809019b84b4f" />



## Tests guidés des fonctions natives

**Objectif :** Vérifier le comportement des fonctions natives JNI (`factorial`, `reverseString`, `sumArray`) avec différents scénarios.

| Test | Fonction | Entrée | Résultat attendu | Commentaire |
|------|---------|--------|-----------------|-------------|
| 1 | `factorial` | `10` | `3628800` | Cas normal, factorielle positive |
| 2 | `factorial` | `-5` | `-1` | Gestion des valeurs négatives |
| 3 | `factorial` | `20` | `-2` | Dépassement d’entier (overflow) |
| 4 | `reverseString` | `""` | `""` | Chaîne vide → résultat vide |
| 5 | `sumArray` | `new int[]{}` | `0` | Tableau vide → somme = 0 |

**Explications :**

1. `factorial(10)` : calcul standard → 10! = 3628800.  
2. `factorial(-5)` : valeur négative non valide → retourne -1.  
3. `factorial(20)` : dépassement de la capacité `int` → retourne -2.  
4. `reverseString("")` : inverser une chaîne vide reste vide.  
5. `sumArray(new int[]{})` : somme d’un tableau vide → 0.  

**Check :** Chaque test couvre un cas classique ou une situation limite, assurant la robustesse des méthodes natives.

---

## Annexes

### Permissions demandées
- `INTERNET`  
- `ACCESS_NETWORK_STATE`  
- `READ_EXTERNAL_STORAGE`

### Composants exportés
- `MainActivity`  
- `NetworkService`  
- `SyncReceiver`

---

## Résultats organisation fichiers
- Fichiers JAR décompilés placés dans `./results`  
- Rapport `rapport.md` sauvegardé dans `./results`  
- Artefacts temporaires supprimés (DEX extraits, APK temporaire)

<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/3a4c6e22-daba-418f-84b7-7ce8f8df267a" />

<img width="1911" height="1077" alt="image" src="https://github.com/user-attachments/assets/71ccc452-e266-4a2e-bbca-1b878ae15090" />

---

## 12. Analyse des logs avec Logcat

Étapes :

- Ouvrir Logcat dans Android Studio
- Filtrer avec le tag : JNI_DEMO

Les logs permettent de suivre l’exécution du code natif et de détecter les erreurs.

<img width="1919" height="322" alt="image" src="https://github.com/user-attachments/assets/1f49bcb6-9037-4697-8149-9cf08dd35d45" />

---

## 13. Gestion des erreurs

Erreur fréquente :

UnsatisfiedLinkError

Causes possibles :

- Bibliothèque non chargée
- Nom incorrect de la bibliothèque
- Mauvaise signature JNI

Solutions :

- Vérifier System.loadLibrary
- Vérifier les noms des fonctions JNI
- Vérifier le package et la classe

---

## 14. Bonnes pratiques JNI

- Limiter les appels entre Java et C++
- Libérer les ressources (ReleaseStringUTFChars, etc.)
- Vérifier les entrées (null, erreurs)
- Éviter les fuites mémoire
- Garder une interface JNI claire et organisée

---

## 15. Extensions possibles

- Intégration de bibliothèques C++ (OpenCV, TensorFlow)
- Optimisation des performances
- Sécurisation du code sensible
- Protection contre le reverse engineering

---

## 16. Conclusion

Ce laboratoire permet de comprendre comment intégrer du code natif dans une application Android via JNI.

Il met en évidence les avantages en termes de performance et de sécurité, tout en soulignant l’importance de bonnes pratiques pour éviter les erreurs et assurer la stabilité de l’application.

JNI reste une solution efficace lorsqu’elle est utilisée de manière maîtrisée.
