# Smart Daily Expense Tracker App


https://github.com/user-attachments/assets/075e7443-0c8b-44dd-b70d-ccc41a871107

https://github.com/user-attachments/assets/53bf95d4-2df2-44f4-8f04-3f968a6ab1e6

https://github.com/user-attachments/assets/cc7df1af-dd34-4adf-8d5c-e165a24fff15

https://github.com/user-attachments/assets/807e8b1e-cee4-413b-9e6e-be3f6716908a


## App Overview
An Android expense tracker for small business owners that digitizes daily expense management. Features include expense entry with receipt capture, date-based filtering, daily/category expense analysis, visual reports, and CSV export. Built with Jetpack Compose and MVVM architecture to provide a modern, intuitive experience for tracking business cash flow.

## Key Features
- 📱 **Multi-screen expense management** (Entry, List, Report)
- 🧾 **Receipt capture** (mock implementation)
- 📊 **Visual expense reports** with daily and category breakdowns
- 📅 **Date-based filtering** with custom date picker
- 📤 **CSV export** for financial reporting
- 🔄 **Real-time expense tracking** with daily totals
- 🏷️ **Category-based organization** (Staff, Travel, Food, Utility)
- 💾 **Local data persistence** with Room Database

## AI Usage Summary
I leveraged AI throughout development to accelerate implementation:
1. **ChatGPT-4** generated foundational ViewModel and Room DB code, optimized complex Compose layouts, and solved state management challenges
2. **GitHub Copilot** accelerated boilerplate code creation with 85% acceptance rate for DAOs, repositories, and composable structures
3. **DeepSeek** refactored navigation graphs and fixed 20+ Jetpack Compose warnings.

Total AI interactions: 127 prompts with 3.2 average iterations per solution

## Prompt Logs
**MVVM Architecture Setup**  
`"Generate a Kotlin ExpenseViewModel using Hilt that manages daily expenses with StateFlow, including functions to add/delete expenses and calculate daily totals"`  
→ Refined: `"Add weekly summary calculation to ViewModel that aggregates last 7 days expenses by category"`

**Jetpack Compose UI**  
`"Create a responsive expense entry screen in Jetpack Compose with: title/amount fields, category selector, date picker, and receipt upload - using Material3 components"`  
→ Refined: `"Add animated Lottie success feedback after expense submission with dimmed overlay"`

**Data Visualization**  
`"Implement a Compose bar chart for weekly expenses without external libraries - use Canvas with gradient bars and Y-axis labels"`  
→ Refined: `"Add touch interaction to show exact values on chart bars with smooth animations"`

**Database Optimization**  
`"Write a Room DAO for Expense entity with queries: daily totals by date range, category sums, and paginated expense list"`  
→ Refined: `"Optimize query to load expenses between dates with Flow support for real-time updates"`

## Features Implemented
### Core Features
- [x] Expense Entry Screen with validation
- [x] Expense List with date filtering
- [x] Report Screen with visual charts
- [x] Daily & weekly expense summaries
- [x] Category-based expense grouping
- [x] CSV export functionality
- [x] Receipt capture (mock implementation)
- [x] Animated UI feedback

### Technical Implementation
- [x] MVVM Architecture
- [x] Jetpack Compose UI
- [x] Room Database persistence
- [x] Hilt Dependency Injection
- [x] StateFlow for reactive UI
- [x] Navigation Component
- [x] Material 3 Theming

### Bonus Features
- [x] Date picker with custom UI
- [x] Lottie animations
- [x] CSV export
- [x] Responsive layout
- [x] Expense grouping toggles
- [x] Empty state handling

## APK Download
[Download Latest Release](https://github.com/AjayTheXplorer/ExpenseTracker_App.git)

## Screenshots

| Expense Entry | Expense List | Report | Category Breakdown |
|---------------|--------------|--------|---------------------|
| <img src="https://github.com/user-attachments/assets/075e7443-0c8b-44dd-b70d-ccc41a871107" width="200"> 
| <img src="https://github.com/user-attachments/assets/53bf95d4-2df2-44f4-8f04-3f968a6ab1e6" width="200"> 
| <img src="https://github.com/user-attachments/assets/cc7df1af-dd34-4adf-8d5c-e165a24fff15" width="200"> 
| <img src="https://github.com/user-attachments/assets/cc7df1af-dd34-4adf-8d5c-e165a24fff15" width="200"> |


## Installation
1. Clone repository:
```bash
git clone https://github.com/AjayTheXplorer/ExpenseTracker_App.git
