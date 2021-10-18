# What
The Business Layer is the place where all the business/domain logic, i.e. rules that are particular to the problem that the application has been built to handle, lives. This might be salary calculations, data analysis modelling, or workflow such as passing a order through different stages.
And Business Logic is what we call pieces of code that carry out operations that are specific to the job that the application is carrying out
![](./images/businessLayer/architecture-definition.png)
- **User Interface Layer (UI)** – where all presentation and user interaction occurs. Data is displayed to the user. The user sends and also receives data.
- **Business Logic Layer (BLL)** – serves as an intermediary for data exchange between the presentation layer and the Data Access Layer
- **Data Access Layer (DAL)** – the layer where data takes place.
# Why
The Business Layer is used in 3 layer architecture as above, or used in DDD(Domain-driven design).
The purpose to doing that is: **Separation of concerns**:
- Which is an ideal world you want any given piece of code to do one thing and to do that well. So you want to try and avoid mixing up display logic and business logic and storage logic and everything else.
- This separation provides a variety of benefits and testability is an important one , but also the fact that the code should be easier to understand at any given level and easier to maintain.
# Domain-driven-design
## What
### Domain
Domain is : `A sphere of knowledge or activity.` More detail, `domain` refers to the subject area on which the application is intended to apply. In other words, during application development, the `domain` is the `sphere of knowledge and activity around which the application logic revolves.`
So domain-logic or domain-layer which maybe better know as `business logic`. The `business logic` of an application refers to the higher-level rules for how `business objects` interact with one another
### Domain-driven-design
DDD is the concept which aims to ease the creation of complex applications by connecting the related pieces of the software into an ever-evolving model. DDD focus in 3 principle:
- Focus on the core domain and domain logic.
- Base complex designs on models of the domain.
- Constantly collaborate with domain experts, in order to improve the application model and resolve any emerging domain-related issues.
## Why
### Advantage:
- Eases Communication
- Improves Flexibility
- Emphasizes Domain Over Interface
### Disadvantages:
- Requires Robust Domain Expertise
- Encourages Iterative Practices
- Suited for Highly Technical Projects