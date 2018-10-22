# InvestorsApi

All URIs are relative to *https://virtserver.swaggerhub.com/doevus/lbloan/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createInvestment**](InvestorsApi.md#createInvestment) | **POST** /investments | creates a new investment

<a name="createInvestment"></a>
# **createInvestment**
> createInvestment(body)

creates a new investment

Creates a new investment into a loan

### Example
```java
// Import classes:
//import invalidPackageName.ApiException;
//import invalidPackageName.InvestorsApi;


InvestorsApi apiInstance = new InvestorsApi();
Investment body = new Investment(); // Investment | The investment to be created
try {
    apiInstance.createInvestment(body);
} catch (ApiException e) {
    System.err.println("Exception when calling InvestorsApi#createInvestment");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Investment**](Investment.md)| The investment to be created | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

