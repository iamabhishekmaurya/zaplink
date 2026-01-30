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
│   ├── (auth)/                   # Auth route group (login, signup, etc.)
│   ├── dashboard/                # Dashboard pages
│   │   ├── analytics/            # Analytics pages
│   │   ├── link/                 # Shortlink management pages
│   │   ├── qr/                   # QR code management pages
│   │   ├── media/                # Media library
│   │   ├── calendar/             # Scheduler/calendar
│   │   └── ...
│   ├── about/                    # About page
│   ├── blogs/                    # Blog pages
│   ├── pricing/                  # Pricing page
│   ├── layout.tsx                # Root layout
│   └── page.tsx                  # Home page
│
├── components/
│   ├── ui/                       # Base UI primitives (shadcn/ui)
│   ├── dashboard/                # Dashboard-specific components
│   │   ├── app-sidebar.tsx
│   │   ├── chart-area-interactive.tsx
│   │   ├── dashboard-charts.tsx
│   │   ├── recent-activity-table.tsx
│   │   ├── section-cards.tsx
│   │   ├── site-header.tsx
│   │   ├── team-switcher.tsx
│   │   └── index.ts              # Barrel export
│   ├── shortlinks/               # Shortlink management components
│   │   ├── search-and-filters.tsx
│   │   ├── share-dropdown.tsx
│   │   ├── short-link-card.tsx
│   │   ├── short-link-empty.tsx
│   │   ├── short-link-error.tsx
│   │   ├── short-link-skeleton.tsx
│   │   └── index.ts              # Barrel export
│   ├── qr/                       # QR code components
│   │   ├── qr-code-card.tsx
│   │   ├── qr-code-skeleton.tsx
│   │   ├── qr-search-and-filters.tsx
│   │   └── index.ts              # Barrel export
│   ├── layout/                   # Layout wrappers (MainLayout, Navbar)
│   ├── common/                   # Shared utilities (ThemeToggle, etc.)
│   ├── auth/                     # Auth components
│   ├── providers/                # All React providers consolidated
│   ├── landing/                  # Landing page sections
│   ├── blog/                     # Blog components
│   ├── calendar/                 # Calendar/scheduler components
│   ├── media/                    # Media manager components
│   ├── (form)/                   # Form components (auth forms)
│   ├── (nav)/                    # Navigation components
│   └── ...                       # Feature-specific folders
│
├── hooks/                        # Custom React hooks
│   ├── useDashboardData.ts
│   ├── useDynamicQrs.ts
│   ├── useMedia.ts
│   ├── useMobile.ts
│   └── useShortlinks.ts
│
├── lib/
│   ├── api/                      # API clients and services
│   │   ├── client.ts             # Axios instance
│   │   ├── auth.ts
│   │   ├── dynamicQr.ts
│   │   ├── scheduler.ts
│   │   ├── shortlinkService.ts
│   │   └── QRServerApi.ts
│   ├── constants/                # Constants and config
│   │   └── apiConstant.ts
│   ├── types/                    # TypeScript types
│   │   └── apiRequestType.ts
│   ├── utils.ts                  # Utility functions (cn, etc.)
│   └── ...
│
├── store/                        # Redux store
│   ├── index.ts
│   └── slices/
│       └── authSlice.ts
│
└── middleware.ts                 # Next.js middleware
```

---

## Contributor Guidelines

### Where to Place New Files

| File Type | Location | Notes |
|-----------|----------|-------|
| New page/route | `src/app/<route>/page.tsx` | Follow App Router conventions |
| Dashboard page | `src/app/dashboard/<feature>/page.tsx` | Use route groups if needed |
| Base UI component | `src/components/ui/` | shadcn/ui primitives only |
| Dashboard component | `src/components/dashboard/` | Add to barrel export |
| Shortlink component | `src/components/shortlinks/` | Add to barrel export |
| QR component | `src/components/qr/` | Add to barrel export |
| Feature-specific | `src/components/<feature>/` | Create new folder if needed |
| Custom hook | `src/hooks/` | Prefix with `use` |
| API service | `src/lib/api/` | One file per service domain |
| TypeScript types | `src/lib/types/` | Shared types only |
| Constants/config | `src/lib/constants/` | Application constants |
| Redux slice | `src/store/slices/` | One slice per domain |

### Import Conventions

```typescript
// Use @/ alias for imports
import { Button } from '@/components/ui/button'
import { ShortLinkCard } from '@/components/shortlinks/short-link-card'
import { useDashboardData } from '@/hooks/useDashboardData'
import { API_ENDPOINTS } from '@/lib/constants/apiConstant'
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
