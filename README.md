# Foodr

**A-Team**  
Developers: Abdul Aziz Bah, Altug Gemalmaz, Alexander Shelley  
Emails: bah0@purdue.edu, mgemalma@purdue.edu, ashelley@purdue.edu  

## Purpose

Foodr is an app designed to help users explore restaurant menus with visual and user-generated content support. Users can see pictures of each menu item, read reviews, and view ratings, making it easier to decide what to order. Unlike broader restaurant review apps like Yelp, Foodr focuses on individual menu items to enhance the dining experience for both customers and restaurant owners.

## Key Features

- **Menu Item Reviews**: Users can write reviews for individual food items rather than the restaurant as a whole, enabling more focused feedback.
- **Visual Menu Exploration**: The app displays appetizing pictures of each food item to aid users in deciding what to order.
- **Automatic Menu Display**: Upon opening, Foodr detects the closest restaurant based on GPS and automatically displays its menu.
- **Owner Insights**: Restaurant owners can analyze which items are most popular and improve their offerings accordingly.

## Design Overview

### Backend

- **Database**: Firebase serves as the backend for secure data storage, including user information and menu data. Its built-in authentication features simplify user login and access control.
- **Data Security**: Firebase secures data by encrypting it during transmission, ensuring safe handling of user-sensitive information.

### User Interface

- **Activities**: Android UI components render all views, including a Google Maps-integrated map view that displays nearby restaurants.
- **Intents**: Dynamic intents are used to display data based on information retrieved from the backend, optimizing user interaction.

### Object Structure

- **Restaurant and Food Items**: Each restaurant has a list of food items. Each food item has a name, description, image, and user reviews.
- **User and Admin Roles**: A user can also be a restaurant owner (admin), who can create and manage menu items for their restaurant. Admins cannot review their own restaurant items to ensure unbiased feedback.

## Design Considerations

### Functional Design

- **Database Choice**: Firebase was chosen over SQL and MongoDB for its ease of integration and rich features.
- **User Identification**: Users are identified using device-based authentication to minimize input requirements.
- **Admin Restrictions**: Admins have limited access to features such as reviewing and rating their menu items, to avoid conflicts of interest.

### Non-Functional Design

- **Server Framework**: Firebase handles all server-related tasks, including authentication, eliminating the need for additional server setup.
- **Class Design**: Admin and regular user roles are merged into one class, with an extra list attribute to manage restaurants for admins, reducing code duplication.

## App Workflow

1. **User Identification**: Upon opening the app, users are identified and directed to the nearest restaurant’s menu.
2. **Menu Display**: Menu items with pictures and reviews are displayed, allowing users to select and review items.
3. **Admin Functionality**: Admins can modify the menu for their restaurant through a dedicated interface, which provides options to add or edit items.

## Screens and Mockups

1. **Registration and Login**: Users can register and log in, with admins having a separate login option.
2. **Map View**: Restaurants are displayed on a map; tapping a restaurant opens its menu.
3. **Menu and Details**: Each restaurant’s menu is presented with images of each item, and tapping on an item shows more details.
4. **Review Submission**: Users can submit or edit reviews for menu items they’ve tried.
5. **Admin Interfaces**: Admins can add new food items, modify the menu, and view customer feedback.
