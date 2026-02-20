# Settings Page Documentation

## Overview
The Settings page provides a centralized hub for user configuration, including account details, privacy preferences, billing, notifications, and more. It is implemented using Next.js App Router and resides at `/dashboard/settings`.

## Structure

### Route
- **Path**: `/dashboard/settings`
- **Redirect**: Accessing the root path redirects to `/dashboard/settings/account`.

### Components
All settings logic is encapsulated within the `src/features/settings` module:
- **Layout**: `SettingsLayout` provides the sidebar navigation.
- **Sidebar**: `SettingsSidebar` renders the navigation links.
- **Forms**: Individual form components for each section (e.g., `AccountForm`, `PrivacyForm`).

### Categories
1. **Account**: Manage profile information (Username, Email, Bio).
2. **Privacy**: Control profile visibility and data sharing.
3. **Billing**: View subscription plan and payment methods.
4. **Notifications**: Configure email and push notification preferences.
5. **Team**: Invite members and manage roles (Admin, Editor, Viewer).
6. **Security**: Two-factor authentication and password management.
7. **Advanced**: Developer options and experimental features.
8. **Support**: Contact support form.

## Integration

### Navigation
- Linked from the main application sidebar under "Settings".
- Accessible via the user dropdown menu (Account, Billing, Notifications).

### State Management
- Uses `react-hook-form` for form handling.
- Uses `zod` for validation.
- Uses `sonner` for toast notifications.

## Extending
To add a new settings category:
1. Create a new form component in `src/features/settings/components`.
2. Create a new page in `src/app/dashboard/settings/[category]/page.tsx`.
3. Add the route to `sidebarNavItems` in `src/features/settings/components/settings-sidebar.tsx`.
