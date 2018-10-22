# OriginatorsApi

All URIs are relative to *https://virtserver.swaggerhub.com/doevus/lbloan/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createLoan**](OriginatorsApi.md#createLoan) | **POST** /loans | creates a new loan
[**deleteLoan**](OriginatorsApi.md#deleteLoan) | **DELETE** /loans/{loanId} | deletes a loan

<a name="createLoan"></a>
# **createLoan**
> createLoan(body)

creates a new loan

Creates a new loan

### Example
```java
// Import classes:
//import invalidPackageName.ApiException;
//import invalidPackageName.OriginatorsApi;


OriginatorsApi apiInstance = new OriginatorsApi();
Loan body = new Loan(); // Loan | The loan to be created
try {
    apiInstance.createLoan(body);
} catch (ApiException e) {
    System.err.println("Exception when calling OriginatorsApi#createLoan");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Loan**](Loan.md)| The loan to be created | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a name="deleteLoan"></a>
# **deleteLoan**
> deleteLoan(loanId)

deletes a loan

Deletes the loan with the specified ID. 

### Example
```java
// Import classes:
//import invalidPackageName.ApiException;
//import invalidPackageName.OriginatorsApi;


OriginatorsApi apiInstance = new OriginatorsApi();
Integer loanId = 56; // Integer | ID of loan to delete
try {
    apiInstance.deleteLoan(loanId);
} catch (ApiException e) {
    System.err.println("Exception when calling OriginatorsApi#deleteLoan");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loanId** | **Integer**| ID of loan to delete |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

