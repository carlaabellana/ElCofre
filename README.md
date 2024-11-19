### El Cofre Shopping Center - Business Model Simulation
This project is a Java application designed to simulate different business models for stores in the "El Cofre" shopping center. 
## 🔎OBJECTIVE
The project focuses on digitizing a local shopping center to explore and validate applicable business models. It was implemented in two phases:
- Initial Phase: Development of an MVP with basic functionalities.
- Final Phase: Expansion of the system to support various product types and business models.
## 🗝️KEY FEATURES
**Cloud Data Persistence:** The system allows saving and retrieving data through a cloud storage service, with a local fallback in case of connection failure.  
This ensures that data can be accessed and modified remotely, providing flexibility and a modern approach to data management. The system:  
- Checks API Connection: At startup, the application verifies the connection to the cloud API. If the connection fails, the system switches to using local JSON files as a backup.
- Handles API Failures Gracefully: If neither the API nor the local files are accessible, the application alerts the user and terminates execution.
- Interacts via Endpoints: Data is accessed and updated using specific API endpoints, enhancing data security and control by ensuring that interactions are managed through controlled routes.  

**Product Categories and Tax Calculation:** 
  - General: 21% VAT.
  - Reduced: 10% (reduced to 5% if the average rating is above 3.5).
  - Super Reduced: 4% (0% if the price exceeds €100).
## 💻EXECUTION
The project runs by checking the connection to the cloud API and, if necessary, using local JSON files (products.json and shops.json).
