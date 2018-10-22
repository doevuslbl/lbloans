# LB Loans

REST API for loan management.

# API Documentation

Public API is available at Swagger, using OpenAPI 3.0: https://app.swaggerhub.com/apis/doevus/lbloan/1.0.0

Note that this may have errors or inaccuracies due to limited QA time.

# Running the application
To run the application, download the source code and run the following command:

```sh
mvn package && java -jar target/lbl-loans-rest-0.1.0.jar
```

# Limitations
- For interest calculations, currently investor IDs need to be provided. These are currently not exposed via API, so they are written to the console directly when creating investments.
- Unfortunately, due to limited time, the interest calculation is not actually accessible via the API. The code is present, however, there is a problem with investor-investment mapping in the database.
- Unit tests are provided but limited in scope due to time constraints.
- The database used is an in-memory database, therefore is not persisted when the application is restarted.

# Assumptions
- Though the model supports multiple rates for different periods, currently interest calculation assumes only one rate is present for each lone.
- The rate calculation logic presumes that the period for which interest calculation is requested falls fully within the loan period. Thus, e.g. if requesting interest calculcation for 1-Jan-2018 to 31-Dec-2018, if loan is active only 1-Jul-2018 to 31-Dec-2018, the interest will be calculated as if it was active the whole year.
- The model allows specifying balloon, but currently this is not taken into account when calculating interest.
- The interest payments are calculated on a yearly basis, and on an interest-only basis. This differs markedly from e.g. Daily Actual Balance loans, where interest would be decreased every time a payment is received, or at the very least would be calculated monthly or quarterly, depending on when payments are made.
- Different frequencies can be specified for loans, however, as no payment schedule logic is currently present, these have no actual effect.
- Currently, all customers are presumed to be new customers when new loans are created. However, investments *are* made into existing loans, using loan IDs.
- Validation is in place, however, it makes some simplifying assumptions to limit scope.
- Dealing with IDs is simplified - these are used when providing a linked object (e.g. expecting loan ID when setting up a new investment into that loan), and when identifying existing objects (e.g. when requesting interest calculations for investors using their IDs).

