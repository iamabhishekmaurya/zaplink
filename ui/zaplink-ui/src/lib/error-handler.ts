import { toast } from 'sonner';

export interface ErrorOptions {
  description?: string;
  duration?: number;
}

export class ErrorHandler {
  static success(title: string, options?: ErrorOptions) {
    toast.success(title, {
      description: options?.description,
    });
  }

  static error(title: string, options?: ErrorOptions) {
    toast.error(title, {
      description: options?.description,
    });
  }

  static warning(title: string, options?: ErrorOptions) {
    toast.warning(title, {
      description: options?.description,
    });
  }

  static info(title: string, options?: ErrorOptions) {
    toast.info(title, {
      description: options?.description,
    });
  }

  static handleApiError(error: any, context: string) {
    let message = 'An unexpected error occurred';
    let description = '';

    if (error?.response?.data?.message) {
      message = error.response.data.message;
    } else if (error?.message) {
      message = error.message;
    }

    if (error?.response?.status === 401) {
      description = 'Please check your authentication and try again';
    } else if (error?.response?.status === 403) {
      description = 'You do not have permission to perform this action';
    } else if (error?.response?.status === 404) {
      description = 'The requested resource was not found';
    } else if (error?.response?.status >= 500) {
      description = 'Server error. Please try again later';
    } else if (error?.code === 'ERR_NETWORK') {
      message = 'Network Error';
      description = 'Unable to connect to the server. Please check your internet connection.';
    }

    toast.error(message, { description });
  }

  static handleValidationError(field: string, message: string) {
    toast.error(`Validation Error`, {
      description: `${field}: ${message}`,
    });
  }
}

// Export convenience functions
export const showSuccessToast = ErrorHandler.success.bind(ErrorHandler);
export const showErrorToast = ErrorHandler.error.bind(ErrorHandler);
export const showWarningToast = ErrorHandler.warning.bind(ErrorHandler);
export const showInfoToast = ErrorHandler.info.bind(ErrorHandler);
export const handleApiError = ErrorHandler.handleApiError.bind(ErrorHandler);
export const handleValidationError = ErrorHandler.handleValidationError.bind(ErrorHandler);
