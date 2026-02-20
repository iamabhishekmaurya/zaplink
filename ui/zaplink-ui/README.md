# Zaplink UI

A modern, production-ready Next.js 16 application for URL shortening, QR code generation, and link analytics.

## Getting Started

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Run linting
npm run lint
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

---

## Project Structure

```
src/
├── app/                          # Next.js App Router (Pages & Layouts)
│   ├── (auth)/                   # Auth route group
│   ├── dashboard/                # Dashboard pages
│   └── ...
│
├── features/                     # Feature-based Architecture
│   ├── auth/                     # Authentication feature
│   │   ├── ui/                   # Feature-specific UI components
│   │   └── hooks/                # Feature-specific hooks
│   ├── dashboard/                # Dashboard feature
│   │   ├── ui/
│   │   └── hooks/
│   ├── landing/                  # Landing page feature
│   └── ... (blog, qr, shortlinks, etc.)
│
├── components/                   # Shared Presentational Components
│   ├── ui/                       # Base UI primitives (shadcn/ui)
│   ├── layout/                   # Global layout components
│   └── common/                   # Common shared components
│
├── services/                     # Centralized API Services
│   ├── auth.ts
│   ├── client.ts
│   └── ...
│
├── hooks/                        # Shared custom hooks
│
├── lib/                          # Utilities and Core Logic
│   ├── constants/                # Centralized constants
│   ├── types/                    # Shared TypeScript types
│   └── utils.ts                  # Helper functions
│
├── store/                        # Redux Global State
│
└── middleware.ts                 # Next.js middleware
```

---

## Contributor Guidelines

### Where to Place New Files

| File Type | Location | Notes |
|-----------|----------|-------|
| New page/route | `src/app/<route>/page.tsx` | Follow App Router conventions |
| Feature Component | `src/features/<feature>/ui/` | If specific to a feature |
| Shared Component | `src/components/ui/` or `src/components/common/` | If used across multiple features |
| API Service | `src/services/` | One file per service domain |
| Constants | `src/lib/constants/` | |
| Types | `src/lib/types/` | Shared types only |

### Import Conventions

```typescript
// Use @/ alias for imports
import { Button } from '@/components/ui/button' // Shared UI
import { LoginForm } from '@/features/auth/ui/LoginForm' // Feature UI
import api from '@/services/client' // Services
import { API_ENDPOINTS } from '@/lib/constants/apiConstant' // Constants
```

### Naming Conventions

- **Files**: `kebab-case.tsx` (e.g., `short-link-card.tsx`)
- **Components**: `PascalCase` (e.g., `ShortLinkCard`)
- **Hooks**: `camelCase` with `use` prefix (e.g., `useShortlinks`)
- **Constants**: `SCREAMING_SNAKE_CASE` (e.g., `API_ENDPOINTS`)

---

## Recent Refactoring Notes

### Removed Files
- `components/layout/Footer.tsx` - Dead code (entirely commented out)
- `lib/api/scheduler-mock.ts` - Unused mock API (real API in scheduler.ts)
- `src/providers/` folder - Consolidated into `components/providers/Providers.tsx`

### Restructured Folders
- `lib/constant/` → `lib/constants/` (naming consistency)
- Loose dashboard components → `components/dashboard/`
- Loose shortlink components → `components/shortlinks/`
- Loose QR components → `components/qr/`

### Consolidated Providers
All providers now in single file: `components/providers/Providers.tsx`
- React Query
- Redux
- Theme (next-themes)
- Toast (sonner)

---

## Future Improvements (TODOs)

1. **Linting & Formatting**
   - Add `import/order` ESLint rule for consistent imports
   - Configure Prettier with pre-commit hooks

2. **Testing**
   - Add Jest/Vitest for unit tests
   - Add React Testing Library for component tests
   - Add Playwright/Cypress for E2E tests

3. **CI/CD**
   - Add dependency audit to CI
   - Add bundle size monitoring
   - Add unused exports detection

4. **Performance**
   - Implement lazy loading for dashboard components
   - Analyze and optimize bundle splitting

---

## Tech Stack

- **Framework**: Next.js 16 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS v4
- **Settings Hub**: Comprehensive user and application settings.
- **UI Components**: shadcn/ui (Radix primitives)
- **State Management**: Redux Toolkit + React Query
- **Forms**: React Hook Form + Zod
- **Icons**: Lucide React
- **Charts**: Recharts
- **Animations**: Motion (Framer Motion)

---

## Learn More

- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS](https://tailwindcss.com)
- [shadcn/ui](https://ui.shadcn.com)
