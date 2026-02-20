import { test, expect } from '@playwright/test';

test.describe('Settings Page', () => {
    test('should navigate to settings and redirect to account', async ({ page }) => {
        await page.goto('/dashboard/settings');
        await expect(page).toHaveURL(/\/dashboard\/settings\/account/);
    });

    test('should display sidebar navigation', async ({ page }) => {
        await page.goto('/dashboard/settings/account');

        await expect(page.getByRole('link', { name: 'Account' })).toBeVisible();
        await expect(page.getByRole('link', { name: 'Privacy' })).toBeVisible();
        await expect(page.getByRole('link', { name: 'Billing' })).toBeVisible();
    });

    test('should allow navigating between tabs', async ({ page }) => {
        await page.goto('/dashboard/settings/account');

        await page.getByRole('link', { name: 'Privacy' }).click();
        await expect(page).toHaveURL(/\/dashboard\/settings\/privacy/);
        await expect(page.getByRole('heading', { name: 'Privacy' })).toBeVisible();
    });
});
