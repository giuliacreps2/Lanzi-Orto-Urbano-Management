# 🌱 Lanzi Orto Urbano

> Piattaforma e-commerce per la vendita di microgreens freschi, con supporto B2B e B2C, sistema a punti fedeltà e gestione avanzata delle scorte tramite etichette di lotto.

---

## 📋 Indice

- [Descrizione del Progetto](#descrizione-del-progetto)
- [Funzionalità Principali](#funzionalità-principali)
- [Architettura](#architettura)
- [Tecnologie](#tecnologie)
- [Struttura del Progetto](#struttura-del-progetto)
- [API Endpoints](#api-endpoints)
- [Sistema di Etichette e Lotti](#sistema-di-etichette-e-lotti)
- [Contribuire](#contribuire)

---

## 📖 Descrizione del Progetto

**Lanzi Orto Urbano** è una piattaforma e-commerce specializzata nella vendita di microgreens freschi. Il sistema è progettato per servire sia clienti privati (B2C) che aziende e rivenditori (B2B), con funzionalità dedicate per ciascun segmento e un sistema di punti fedeltà condiviso.

La piattaforma integra un sistema di gestione magazzino basato su **etichette di lotto**, che consente la tracciabilità completa del prodotto dalla semina alla consegna, aggiornando in tempo reale sia le scorte interne che le informazioni visibili al cliente.

---

## ✨ Funzionalità Principali

### 👤 B2C — Clienti Privati
- Navigazione e acquisto del catalogo microgreens
- Registrazione e gestione del profilo personale
- Sistema a punti fedeltà (accumulo e utilizzo)
- Storico ordini e tracciamento spedizioni
- Visualizzazione lotto e data di semina per ogni prodotto

### 🏢 B2B — Clienti Aziendali
- Accesso a listini prezzi dedicati e riservati
- **Riordino automatico**: l'admin può configurare ordini periodici ricorrenti per clienti che richiedono telefonicamente sempre lo stesso ordine
- Gestione account aziendale con più utenti (referenti)
- Fatturazione dedicata
- Sistema a punti condiviso con il segmento B2C

### 🏆 Sistema a Punti (B2B + B2C)
- Accumulo punti su ogni acquisto
- Soglie e premi configurabili dall'admin
- Storico movimenti punti per ogni utente

### 🏷️ Gestione Scorte & Ordini tramite Etichette
- Generazione automatica di etichette per ogni lotto di semina
- Ogni etichetta riporta: numero lotto, data di semina, varietà, quantità
- Aggiornamento automatico del magazzino alla scansione/registrazione dell'etichetta
- Notifica al cliente con le informazioni di lotto del prodotto acquistato
- Tracciabilità completa dal campo alla consegna

### 🔐 Sicurezza & Autenticazione
- Autenticazione basata su JWT tramite **Spring Security**
- Ruoli distinti: `ADMIN`, `B2B`, `B2C`
- Accesso alle risorse filtrato per ruolo

---

## 🏗️ Architettura

```
┌─────────────────────────────────────────────────────┐
│                    CLIENT LAYER                     │
│                Next.js + Tailwind                   │
└─────────────────────┬───────────────────────────────┘
                      │ REST API (HTTPS)
┌─────────────────────▼───────────────────────────────┐
│                   BACKEND LAYER                     │
│              Java 17+ · Spring Boot                 │
│   Spring Security · Spring Data JPA · Specification │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│                  DATABASE LAYER                     │
│                   PostgreSQL                        │
└─────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologie

### Backend
| Tecnologia | Utilizzo |
|---|---|
| Java 17+ | Linguaggio principale |
| Spring Boot | Framework applicativo |
| Spring Security | Autenticazione e autorizzazione (JWT) |
| Spring Data JPA + Specification | Persistenza e query dinamiche |
| PostgreSQL | Database relazionale |
| Maven | Build e gestione dipendenze |

### Frontend
| Tecnologia | Utilizzo |
|---|---|
| Next.js | Framework UI |
| Tailwind CSS | Styling e componenti |

---
 
## 🔗 Links
 
| | Link |
|---|---|
| 🖥️ Frontend Repository | [github.com/giuliacreps2/lanzi-orto-urbano-microgreens](https://github.com/giuliacreps2/lanzi-orto-urbano-microgreens) |
 
---

## 📡 API Endpoints

### Autenticazione
| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/api/auth/register` | Registrazione nuovo utente |
| POST | `/auth/auth/login` | Login e ottenimento JWT |
| POST | `/auth/register/b2c` | Registrazione nuovo utente B2C con mail di verifica |
| POST | `/auth/verify/b2b?token=` | Registrazione nuovo utente B2B con verifica di P.IVA |
| GET | `/auth/b2b/{userId}/approve` | ADMIN | Approvazione profilo utente B2B |

### Prodotti
| Metodo | Endpoint | Descrizione |
|---|---|---|
| GET | `products` | Lista prodotti (pubblica) |
| GET | `products/{productId}` | Dettaglio prodotto con info lotto |
| POST | `products/new-composite` | ADMIN | Crea prodotto composto dalle variabili di prezzo e packaging |

### Ordini
| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/orders/checkout` | Authenticated | Crea nuovo ordine |
| PATCH | `/orders/{orderId}/apply-loyalty` | ADMIN | Applica lo sconto punti sull'ordine |
| POST | `/orders/{orderId}/admin-reorder/{userId}` | ADMIN | Attiva riordino automatico B2B (ADMIN) |

### Etichette
| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/labels/new-lab` | ADMIN | Crea una nuova etichetta manualmente |
| GET | `/labels/order/{orderId}` | ADMIN | Lista di tutte le etichette per ordine |
| POST | `/labels/order/{orderId}/generate` | ADMIN | Genera etichette per ordine |

---

## 🏷️ Sistema di Etichette e Lotti

Ogni ciclo di produzione genera un **lotto di semina** identificato da:

| Campo | Descrizione |
|---|---|
| `labelId` | Codice etichetta |
| `barCodeGs1` |GS1-128 codice per la tracciabilità dei prodotti |
| `Map<String, Object> metadataProdCategory` | Caratterstiche associate alla categoria |
| `Map<String, Object> technicalDetails` | Caratteristiche associate alla variante di prodotto |

Le etichette generate da ogni lotto vengono associate agli ordini in uscita. Quando un'etichetta viene registrata come "spedita":
1. Il magazzino si aggiorna automaticamente (scalando la quantità disponibile)
2. Il cliente riceve notifica con i dettagli del lotto del suo ordine

---

*Lanzi Orto Urbano — Microgreens freschi, dalla semina alla tua tavola. 🌿*

---
---

# 🌱 Lanzi Orto Urbano

> E-commerce platform for selling fresh microgreens, with B2B and B2C support, a shared loyalty points system, and advanced stock management through batch labels.

---

## 📋 Table of Contents

- [Project Description](#project-description)
- [Main Features](#main-features)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints-1)
- [Label & Batch System](#label--batch-system)

---

## 📖 Project Description

**Lanzi Orto Urbano** is an e-commerce platform specializing in the sale of fresh microgreens. The system is designed to serve both private customers (B2C) and businesses/resellers (B2B), with dedicated features for each segment and a shared loyalty points system.

The platform integrates a **batch label-based warehouse management system**, enabling full product traceability from sowing to delivery, updating both internal stock and customer-facing information in real time.

---

## ✨ Main Features

### 👤 B2C — Private Customers
- Browse and purchase microgreens catalog
- User registration and profile management
- Loyalty points system (earn and redeem)
- Order history and shipment tracking
- View batch number and sowing date for each product

### 🏢 B2B — Business Customers
- Access to dedicated, reserved price lists
- **Automatic reorder**: the admin can configure recurring orders for customers who regularly request the same order by phone
- Business account management with multiple users (contact persons)
- Dedicated invoicing
- Shared loyalty points system with B2C segment

### 🏆 Loyalty Points System (B2B + B2C)
- Points earned on every purchase
- Configurable thresholds and rewards by admin
- Points movement history for every user

### 🏷️ Stock & Order Management via Labels
- Automatic label generation for each sowing batch
- Each label includes: batch number, sowing date, variety, quantity
- Automatic stock update upon label scan/registration
- Customer notification with batch details for their purchase
- Full traceability from field to delivery

### 🔐 Security & Authentication
- JWT-based authentication via **Spring Security**
- Distinct roles: `ADMIN`, `B2B`, `B2C`
- Role-based resource access control

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│                    CLIENT LAYER                     │
│                Next.js + Tailwind                   │
└─────────────────────┬───────────────────────────────┘
                      │ REST API (HTTPS)
┌─────────────────────▼───────────────────────────────┐
│                   BACKEND LAYER                     │
│              Java 17+ · Spring Boot                 │
│   Spring Security · Spring Data JPA · Specification │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│                  DATABASE LAYER                     │
│                   PostgreSQL                        │
└─────────────────────────────────────────────────────┘
```

---

## 🛠️ Technologies

### Backend
| Technology | Usage |
|---|---|
| Java 17+ | Main language |
| Spring Boot | Application framework |
| Spring Security | Authentication & authorization (JWT) |
| Spring Data JPA + Specification | Persistence and dynamic queries |
| PostgreSQL | Relational database |
| Maven | Build and dependency management |

### Frontend
| Technology | Usage |
|---|---|
| Next.js | UI Framework |
| Tailwind CSSì | Styling and components |

---
 
## 🔗 Links
 
| | Link |
|---|---|
| 🖥️ Frontend Repository | [github.com/giuliacreps2/lanzi-orto-urbano-microgreens](https://github.com/giuliacreps2/lanzi-orto-urbano-microgreens) |
 
---

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login and get JWT |
| POST | `/auth/register/b2c` | Register new B2C user (sends verification email) |
| POST | `/auth/register/b2b` | Public | Register new B2B profile (sends verification email) |
| POST | `/auth/verify/b2b?token=` | Verify B2B VAT number and submit for admin approval |
| GET | `/auth/b2b/{userId}/approve` | ADMIN | Approve B2B profile |


### Products
| Method | Endpoint | Description |
|---|---|---|
| GET | `products` | Product list (public) |
| GET | `products/{productId}` | Product detail with batch info |
| POST | `products/new-composite` | ADMIN | Create a product composed of price and packaging variables |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders/checkout` | Authenticated | Create new order from cart |
| PATCH | `/orders/{orderId}/apply-loyalty` | ADMIN | Apply loyalty discount to an order |
| POST | `/orders/{orderId}/admin-reorder/{userId}` | ADMIN | Reorder on behalf of a B2B client |

### Labels
| Method | Endpoint | Description |
|---|---|---|
| POST | `/labels/new-lab` | ADMIN | Create a new label manually |
| GET | `/labels/order/{orderId}` | ADMIN | Generate labels for an order |
| POST | `/labels/order/{orderId}/generate` | ADMIN | Get barcode image (PNG, GS1-128) for label |

---

## 🏷️ Label & Batch System

Each production cycle generates a **sowing batch** identified by:

| Field | Description |
|---|---|
| `labelId` | Unique label code |
| `barCodeGs1` | GS1-128 code for product traceability  |
| `Map<String, Object> metadataProdCategory` | Characteristics associated with the category |
| `Map<String, Object> technicalDetails` | Features associated with the product variant |

Labels generated from each batch are linked to outgoing orders. When a label is registered as "shipped":
1. The warehouse updates automatically (reducing available quantity)
2. The customer receives a notification with the batch details of their order

---

*Lanzi Orto Urbano — Fresh microgreens, from seed to your table. 🌿*
