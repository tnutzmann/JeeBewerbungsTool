---
theme: gaia
_class: lead
paginate: true
backgroundImage: url('https://marp.app/assets/hero-background.svg')
marp: true
---
# **J🔍bFinder**
## Technisches Konzept

**von:** Matthias Enderlein, Tony Nutzmann, Max Riedel

---

# Team
- agli
- github zentriert
- 4 Augenprinzip

---

# Klassen --> UML 
- User
  - Name usw.
  - Academic Background
  - Skills[]
  - Bewerbungen

- Skill
  - Name
  - Type
  - Description

- Company
  - Name, Description usw.
  - Jobs[]

- Job
  - Title, Description, Location usw.
  - Tags[]
  - Skills/Requirements[]
  - Bewerber[]
  - state: Enum

- Tag
  - Name

---
# Recomendation-AI 
- Selling Point
- Fancy
- Hot
- alot if-else
- maybe some Java-Streams
- no neural network
