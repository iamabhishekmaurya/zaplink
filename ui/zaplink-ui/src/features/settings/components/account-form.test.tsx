import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { AccountForm } from './account-form'
import { describe, it, expect, vi } from 'vitest'

// Mock toast
vi.mock('sonner', () => ({
    toast: {
        success: vi.fn(),
    },
}))

describe('AccountForm', () => {
    it('renders correctly', () => {
        render(<AccountForm />)
        expect(screen.getByLabelText(/username/i)).toBeInTheDocument()
        expect(screen.getByLabelText(/email/i)).toBeInTheDocument()
    })

    it('validates required fields', async () => {
        render(<AccountForm />)

        const submitButton = screen.getByRole('button', { name: /update account/i })
        fireEvent.click(submitButton)

        // Validation messages should appear (if fields are empty/invalid based on default values)
        // Default values are valid in this form, so we might need to clear them to test required

        const emailInput = screen.getByLabelText(/email/i)
        fireEvent.change(emailInput, { target: { value: '' } })
        fireEvent.click(submitButton)

        await waitFor(() => {
            expect(screen.getByText(/please select an email to display/i)).toBeInTheDocument()
        })
    })
})
