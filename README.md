# WeatherResearch-App
A Java-based command-line tool for retrieving real-time weather data, forecasts, and severe weather alerts, designed for storm chasers, weather researchers, and meteorology enthusiasts. 
## Overview
The Weather Resrarch Application fetches weather information using the National Weather Service (NWS) API and converts ZIP codes to cooridnates via the Nominatim API. It supports user authentication, favoriting locations, and storing preferences in an SQLITE database. Inspried by a personal passion for meteorology, this project was done within a group setting, combining computer science, software engineering concepts and weather research to provide a tool for weather researchers and storm chasers alike to use in the field. 
## Features 
- Location-Based Weather: Retrieve current conditions (e.g., temperature, chances of precipitation) by ZIP code.
- Multi-day Forecasts: Access 7-day weather forecasts for planning storm-chasing outings.
- Severe Weather Alerts: Fetch real-time alerts from NWS for safety and awareness.
- User Authentication: Secure login system with account creation.
- Favorite Locations: Save SIP codes for quick weather lookups.
- Modular Design: Built with high cohesion and low coupling for maintainability.
## Software Engineering Principles
This project applies key software engineering concepts (does not cover all instances of each concept; this list just serves as an example):
- Managers
  - DatabaseManager: Handles SQLite operations for user data and preferences.
  - UserManager: Manages authentication and user settings.
- Factories
  - WeatherDataSourceFactory: Generates weather data sources
  - WeatherDataFactory: Creates weather data objects based on JSON input
  - SQLiteDatabaseFactory: Produces DatabaseManager instances for SQLite
- High Cohesion
  - WeatherService: Single responsiblility of processing weather data (parsing JSON data from the NWS API)
- Low Coupling
  - AbstractDatabaseManager: Allows swapping database implementations without affecting the dependent classes
- Singleton Pattern
  - DatabseHelper: Ensures a single, globally accessible database oconnection instance
- Abstraction
  - WeatherService: Retrieves weather data without exposing details on how the data is fetched, nor how the APIs are formatted
- Encapsulation
  - DatabaseHelper: Encapsulates database connection logic, only exposing necessary methods
- Single Responsibility Principle
  - UserManager: adheres only to the defined responsibility of managing user-related operations
## Architecture
The application follows a modular and extensible 
