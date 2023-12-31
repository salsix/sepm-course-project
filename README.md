# SEPM Course Project: Ticketline
## Context
This was the final project for the course "Software Engineering and Project Management" from my Business Informatics bachelor programme. It was executed as a Team of 5.

## Project Description
We built Ticketline: A ticket reservation and booking system for event venues. We decided to adhere to a strictly separated multilayer architecture to guarantee for easy maintenance and exchangeability of components. Furthermore, the frontend was developed as a standalone application, communicating with the backend solely via a RESTful API. This opened the possibility to add a native app at a later stage or to provide a public API for clients/partners.

The design of the database closely followed the specification of the business logic. I started with designing an entity relation diagram, modeling the actual relations between the real-world entities. The initial draft was discussed with the product owner and adapted as needed. A database diagram was created in the next step. We focused on the funtamental parameters like the type and magnitude of relationships. To validate the design, we formulated the database queries for the most complex parts of the business logic. When we realized a query would be inefficient or overly complicated, we reconsidered the structure of that part and adapted the schema accordingly. For the implementation of the database, we chose to use JPA, due to its ease of use and the great compatibility with our framework (Spring).
