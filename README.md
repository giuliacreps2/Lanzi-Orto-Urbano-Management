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
│           React / Next.js + Tailwind/Bootstrap      │
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
| React / Next.js | Framework UI |
| Tailwind CSS / Bootstrap | Styling e componenti |

---

## 📡 API Endpoints

### Autenticazione
| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/api/auth/register` | Registrazione nuovo utente |
| POST | `/api/auth/login` | Login e ottenimento JWT |

### Prodotti
| Metodo | Endpoint | Descrizione |
|---|---|---|
| GET | `/api/prodotti` | Lista prodotti (pubblica) |
| GET | `/api/prodotti/{id}` | Dettaglio prodotto con info lotto |
| POST | `/api/admin/prodotti` | Crea prodotto (ADMIN) |

### Ordini
| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/api/ordini` | Crea nuovo ordine |
| GET | `/api/ordini/miei` | Storico ordini utente |
| POST | `/api/admin/ordini/riordino/{clienteId}` | Attiva riordino automatico B2B (ADMIN) |

### Punti
| Metodo | Endpoint | Descrizione |
|---|---|---|
| GET | `/api/punti/saldo` | Saldo punti utente |
| GET | `/api/punti/storico` | Movimenti punti |

### Etichette & Lotti
| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/api/admin/lotti` | Crea nuovo lotto di semina (ADMIN) |
| GET | `/api/admin/lotti` | Lista tutti i lotti (ADMIN) |
| POST | `/api/admin/etichette/genera/{lottoId}` | Genera etichette per lotto (ADMIN) |

---

## 🏷️ Sistema di Etichette e Lotti

Ogni ciclo di produzione genera un **lotto di semina** identificato da:

| Campo | Descrizione |
|---|---|
| `numeroLotto` | Codice univoco del lotto (es. `MG-2024-001`) |
| `dataSemina` | Data in cui i semi sono stati piantati |
| `varietà` | Tipo di microgreen (es. Ravanello, Pisello, Basilico) |
| `quantitàPrevista` | Quantità stimata a raccolto (g / vaschette) |
| `dataRaccoltaPrevista` | Data stimata di raccolta |

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
│           React / Next.js + Tailwind/Bootstrap      │
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
| React / Next.js | UI Framework |
| Tailwind CSS / Bootstrap | Styling and components |

---

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT |

### Products
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/prodotti` | Product list (public) |
| GET | `/api/prodotti/{id}` | Product detail with batch info |
| POST | `/api/admin/prodotti` | Create product (ADMIN) |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/ordini` | Create new order |
| GET | `/api/ordini/miei` | User order history |
| POST | `/api/admin/ordini/riordino/{clienteId}` | Trigger B2B automatic reorder (ADMIN) |

### Points
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/punti/saldo` | User points balance |
| GET | `/api/punti/storico` | Points movement history |

### Labels & Batches
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/admin/lotti` | Create new sowing batch (ADMIN) |
| GET | `/api/admin/lotti` | List all batches (ADMIN) |
| POST | `/api/admin/etichette/genera/{lottoId}` | Generate labels for batch (ADMIN) |

---

## 🏷️ Label & Batch System

Each production cycle generates a **sowing batch** identified by:

| Field | Description |
|---|---|
| `batchNumber` | Unique batch code (e.g. `MG-2024-001`) |
| `sowingDate` | Date the seeds were planted |
| `variety` | Microgreen type (e.g. Radish, Pea, Basil) |
| `expectedQuantity` | Estimated harvest quantity (g / trays) |
| `expectedHarvestDate` | Estimated harvest date |

Labels generated from each batch are linked to outgoing orders. When a label is registered as "shipped":
1. The warehouse updates automatically (reducing available quantity)
2. The customer receives a notification with the batch details of their order

---

*Lanzi Orto Urbano — Fresh microgreens, from seed to your table. 🌿*
