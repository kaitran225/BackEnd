# Function Mapping Document

## Authentication and Registration Module

| No | Function Name | Description | Pre-Condition |
|----|--------------|-------------|---------------|
| 1 | Student Registration | Register new student with required information | None |
| 2 | Parent Registration | Register new parent with required information | None |
| 3 | Psychologist Registration | Register new psychologist (admin only) | Admin access required |
| 4 | User Login | Authenticate user and return JWT tokens | Valid user account exists |
| 5 | Token Refresh | Get new access token using refresh token | Valid refresh token |
| 6 | Password Reset | Send password reset email to user | Valid user account exists |
| 7 | Reset Password | Reset password using token from email | Valid reset token |
| 8 | User Logout | Logout current user and invalidate tokens | User must be logged in |
| 9 | Role Validation | Validate user roles and permissions | User must be logged in |
| 10 | User Profile Management | Manage user profile information | User must be logged in |

## Program Management Module

| No | Function Name | Description | Pre-Condition |
|----|--------------|-------------|---------------|
| 11 | Get All Programs | List all programs with role-based access | User must be logged in |
| 12 | Get Program Details | View detailed information of specific program | User must be logged in |
| 13 | Get Program Status | Check current status of a program | User must be logged in |
| 14 | Get Facilitator Programs | View programs assigned to a facilitator | Manager/Psychologist access |
| 15 | Get Enrolled Programs | View programs enrolled by a student | Student/Manager access |
| 16 | Program Registration | Register student in a program | Student/Manager access |
| 17 | Program Cancellation | Cancel program registration | Student/Manager access |
| 18 | Program Tag Management | Manage program tags and categories | Manager access |
| 19 | Participant Management | Manage program participants | Manager/Psychologist access |
| 20 | Program Progress Tracking | Track student progress in programs | Manager/Psychologist access |

## Survey Management Module

| No | Function Name | Description | Pre-Condition |
|----|--------------|-------------|---------------|
| 21 | Create Survey | Create new survey with questions | Manager/Psychologist access |
| 22 | View Survey | View survey details and questions | User must be logged in |
| 23 | Submit Survey | Submit survey responses and get score | Student access |
| 24 | View Survey Results | View survey results and analysis | Manager/Psychologist access |
| 25 | View Student Results | View individual student survey results | Manager/Psychologist access |
| 26 | Survey Question Management | Manage survey questions and options | Manager/Psychologist access |
| 27 | Survey Status Management | Activate/deactivate surveys | Manager/Psychologist access |
| 28 | Standardized Survey Types | Manage standardized survey categories | Manager access |
| 29 | Low Scoring Analysis | Identify students needing attention | Manager/Psychologist access |
| 30 | Appointment Suggestions | Generate appointment suggestions based on results | Manager/Psychologist access |

## Appointment Booking Module

| No | Function Name | Description | Pre-Condition |
|----|--------------|-------------|---------------|
| 31 | Time Slot Management | Manage available time slots, capacity, and booking status | Manager/Psychologist access |
| 32 | Book Appointment | Book a new appointment with a psychologist | Student access |
| 33 | Cancel Appointment | Request cancellation of an appointment with reason | User must be logged in |
| 34 | Check-in Appointment | Mark appointment as started (Psychologist only) | Psychologist access |
| 35 | Check-out Appointment | Complete appointment with notes and status | Psychologist access |
| 36 | View All Appointments | List all appointments with role-based access | User must be logged in |
| 37 | View Appointment Details | View specific appointment details | User must be logged in |
| 38 | Update Appointment | Modify appointment time slot and notes | User must be logged in |
| 39 | Appointment Status Management | Track appointment status changes | User must be logged in |
| 40 | Appointment History | View appointment history and records | User must be logged in |

##   Module

| No | Function Name | Description | Pre-Condition |
|----|--------------|-------------|---------------|
| 61 | Student Management | Manage student information and profiles | Manager/Psychologist access |
| 62 | Parent Management | Manage parent information and profiles | Manager access |
| 63 | Psychologist Management | Manage psychologist information and profiles | Manager access |
| 64 | User Search | Search users by various criteria | Manager access |
| 65 | User Statistics | View user-related statistics | Manager access |
| 66 | User Activity Tracking | Monitor user activities | Manager access |
| 67 | User Status Management | Manage user account status | Manager access |
| 68 | User Permissions | Manage user access permissions | Manager access |
| 69 | User Reports | Generate user-related reports | Manager access |
| 70 | User Analytics | Analyze user behavior and patterns | Manager access | 