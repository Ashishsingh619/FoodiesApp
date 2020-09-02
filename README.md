# FoodiesApp
In these App I have Used Kotlin Language to create my app.</br>
* The App starts With a splash Screen(Main Activity) consisting of the image and name of the app ,which i have created using Handler().postdelayed with an screen time of 2000 seconds.<br/>
* The loginActivity is displayed ,Inorder to use the app the user must register(RegisterActivity) first Where he need to enter the required details, the response is sent to the server using Volley Library and creating jsonrequestObject with the token and content-type of the server in the getheaders which returns a response Sucess =true if the user is successfully  or a value false if the user was not registered Successfully.<br/>
* If the User forgets the password then he can reset the password by clicking the Forget password,then filling the  phone number and email in the Reset Actvity which will send a json request to the server using Json library which will inturn send a OTP to the Regsitered Email which the user Need to enter in the page(OtpActivity) asking for it.If the OTP entered is valid then it will set the users new password.<br/>
* The user needs to enter the regsitered phone number and password to login in the LoginActivity,which send a Json request to the server using volley library and if the login credentails are right it send a succeess as a response message then the user login is succesfull and is directed to the corresponding page.<br/>
* The user is directed to the next page if the login is successful,I have created a navigation drawer(Navigation Activity) using the Drawer layout and Navigation view even used design support library with lot of menu items in the navigation drawer to choose from:<br/>
1. Home
2. MyProfile
3. Favourite Restaurants 
4. orderHistory
5. FAQs
6. LogOut<br/>
* I have Created fragments for each of the Menu Items
* The app Opens with an List of Restaurants which is a part of home menu item, which i have created using RecylerView and respective Homeadapter class in which the list of Restaurants is fetched from json using Get Request using Volley library.<br/>
* If any Restaurants is clicked Then the user is directed to the Resdetail activity Which contains the list of food items created using RecylerView and ResItemAdapter in which the list of items is fetched from Json Get Request using volllry library<br/>
* I have created an local database called FoodDatabase created using Room persistant Library which has different methods like InsertFood,DeleteFood,findFoodById,deleteOrders there is a button to add a order to the cart ,first the findFoodById will check for the item which exists in the database, if it does the button will display Remove or Add if doesn't exist in database,If user clicks the ADD button then the InsertFood Method is called and it adds the items to the database changing the button to remove,If the user clicks the Remove button then DeleteFood is called and that particular item is removed from databse changing the button to add, all these operation are performed using AsyncTask so that the WorkerThread can fetch the data and parallely the Main thread can display the UI<br/>
* There is a Button as Procced to Cart when clicked displays CartActivity which user recylerView and CartAdapter to display the list of items which are added to the cart, the list of items added to cart are fetched from data base using getAllFood method which returns a list of book added to the database.These operation are performed using AsyncTask so that the WorkerThread can fetch the data and parallely the Main thread can display the UI<br/>
* There is Button at the bottom of the cartAvtivity page which displays the totalcost of the foodItems to be paid along with a message to place the order if the user clicks place order then a Json Post Request is send using Volley library to the server Which send an response whether the order is placed or not,If it is then it takes to the OrderplaceActivity which calls the deleteOrder of the roomdatabase and deleted all the items which were added to the cart.</br>
* If the user presses the back Button at any of the previously stated activity it checks if the foodDatabase has any items or not if doesnt have any item then it simply goes to previous activity ,if the user had added the items to the cart then it displays an alert box which stats that if user clicks clear cart the item ,added to cart will be deleted and the user will be taken to previous activity.<br/>
* In each activity i have used Connectivity manager to check if the device is connected to internet,if the device is not then a dialog box appears to tell user about it and if the user clicks go to setting then the user will be taken to wireless setting to turn on the preferred internet connection(wifi,data).<br/>
* When the list of Restaurants is displayed there is heart button to add the items to the Favourite section of the app. For this i have created another database called Restaurant database which has methods like addRestaurant,deleteRestaurants,getRestaurantById,getRestaurantsFav.These operation are performed using AsyncTask so that the WorkerThread can fetch the data and parallely the Main thread can work on the UI<br/>
* When the HomeFragment is called it is checked whether any Restaurant is added to the database or not if it is then A filled heart image is used to indicate that the item is added to the database.If the user clicke the heart Icon then it is checked whether the item already exists in the database if it doesnot then the addRestaurants method is used to add the restaurant to the database and the heart is replaced by filled one,If the user clicks the filled heart again then deleteRestaurant is used to delete the resaurant from database after checking if it exists in database.<br/>
* The user can see there favourites restaurant in the Favourite Restaurants section,In which i have used Recylerview along with FavAdapter to display the list of favourite Restaurants, the list of items is fetched using getRestaurantsFav method.<br/>
* The user can keep track of the previous orders by going in the orderHistory Section, In which i have used nesed recylerView along with the RestNameCartAdapter and CartHisItemAdapter as the nested adapter.The data is fetched from server using Json Get request.<br/>
* There is a option in the toolbar in Which the user can sort the list of Restaurants according to Price and Rating for which i have used Comparator class of the Kotlin and Collection class of java.<br/>
* There is an FAQs section in which i have used hardcored strong to describe the app.<br/>
* The user can logOut by Clicking the logOut item of the navigation drawer which brings the user back to login page,Where the user can login again if user wants to use the app again.<br/>
## SnapShot of FoodiesApp:-
</br>
<table>
 <tr><td><img width="170" alt="SplashScreen" src="https://user-images.githubusercontent.com/62636670/91996950-311ad080-ed57-11ea-996f-07fb9f5fb655.PNG"></td>
    <td><img width="170" alt="LoggingPage" src="https://user-images.githubusercontent.com/62636670/91997572-dc2b8a00-ed57-11ea-8124-d7ea1773bf5f.PNG"></td>
    <td><img width="170" alt="RegisterPage" src="https://user-images.githubusercontent.com/62636670/91998364-bf438680-ed58-11ea-8bf7-12fa79b502a9.PNG"></td>
   <td><img width="170" alt="ResetPage" src="https://user-images.githubusercontent.com/62636670/91998482-e39f6300-ed58-11ea-8bbd-fe8a7b591b08.PNG"></td>
 </tr>
  <tr>
 <td><img width="170" alt="OtpPage" src="https://user-images.githubusercontent.com/62636670/91998853-61636e80-ed59-11ea-8e9b-885ba07d0319.PNG"></td>
 <td><img width="170" alt="HomePage" src="https://user-images.githubusercontent.com/62636670/91998783-498bea80-ed59-11ea-97f6-37dcdedf6677.PNG"></td>
<td> <img width="170" alt="NavigationDrawer" src="https://user-images.githubusercontent.com/62636670/91998972-81932d80-ed59-11ea-8b16-b54e5d871f3d.PNG"></td>
<td><img width="170" alt="FoodItems" src="https://user-images.githubusercontent.com/62636670/91999177-ba330700-ed59-11ea-8abf-00924bb9e064.PNG"></td>
</tr>
 <tr>
   <td><img width="170" alt="Cart" src="https://user-images.githubusercontent.com/62636670/91999427-fe260c00-ed59-11ea-970a-d1d3657d0388.PNG">
</td>
   <td><img width="170" alt="OrderPlaced" src="https://user-images.githubusercontent.com/62636670/91999482-126a0900-ed5a-11ea-886e-8e3dd1b683db.PNG">
</td>
   <td><img width="170" alt="OrderHistory" src="https://user-images.githubusercontent.com/62636670/91999542-244bac00-ed5a-11ea-8e3f-aa9c7617e312.PNG">
</td>
   <td><img width="170" alt="FavotitesRestaurants" src="https://user-images.githubusercontent.com/62636670/91999646-3fb6b700-ed5a-11ea-865e-ea0de4f51930.PNG">
 </td>
  </tr>
  <tr>
    <td>
<img width="170" alt="FavoutieRestaurants" src="https://user-images.githubusercontent.com/62636670/91999822-7096ec00-ed5a-11ea-9785-5f67300aed87.PNG">
</td>
    <td><img width="170" alt="FAQs" src="https://user-images.githubusercontent.com/62636670/91999867-80aecb80-ed5a-11ea-8d2c-1e1892ed974b.PNG">
</td>
    <td><img width="170" alt="Comparator" src="https://user-images.githubusercontent.com/62636670/91999950-97edb900-ed5a-11ea-990d-bba1e5c5e23b.PNG">
</td>
    <td><img width="170" alt="InternetConnection" src="https://user-images.githubusercontent.com/62636670/92000020-adfb7980-ed5a-11ea-97bb-914d28ff865a.PNG">
</td>
  </tr>
  <tr><td><img width="170" alt="LogOut" src="https://user-images.githubusercontent.com/62636670/92000137-cd92a200-ed5a-11ea-98dc-d5bd2f594841.PNG">
</td>
 </tr>
</table>
 <img width="420" alt="OtpEmail" src="https://user-images.githubusercontent.com/62636670/92000226-e8651680-ed5a-11ea-8aae-0f037e73e189.PNG">


