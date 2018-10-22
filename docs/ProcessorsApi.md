# ProcessorsApi

All URIs are relative to *https://virtserver.swaggerhub.com/doevus/lbloan/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**retrieveLoan**](ProcessorsApi.md#retrieveLoan) | **GET** /loans/{loanId} | gets a loan and its investments

<a name="retrieveLoan"></a>
# **retrieveLoan**
> Loan retrieveLoan(loanId)

gets a loan and its investments

Retrieves the loan with the specified ID and displays the details of the loan and its investments. 

### Example
```java
// Import classes:
//import invalidPackageName.ApiException;
//import invalidPackageName.ProcessorsApi;


ProcessorsApi apiInstance = new ProcessorsApi();
Integer loanId = 56; // Integer | ID of loan to return
try {
    Loan result = apiInstance.retrieveLoan(loanId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ProcessorsApi#retrieveLoan");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loanId** | **Integer**| ID of loan to return |

### Return type

[**Loan**](Loan.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

