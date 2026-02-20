import { BioPageLinkType } from '@/features/bio-page/types';

export interface LinkFormData {
  title: string;
  url?: string;
  type: BioPageLinkType;
  isActive: boolean;
  price?: number;
  currency?: string;
  scheduleFrom?: Date | null;
  scheduleTo?: Date | null;
  thumbnailUrl?: string;
  iconUrl?: string;
  // Metadata fields flattened for form
  description?: string;
  sku?: string;
  embedCode?: string;
  gateType?: 'email' | 'password' | 'payment';
  gateValue?: string;
  gateMessage?: string;
}

export interface BioLinkRequestData {
  page_id: string;
  title: string;
  url?: string;
  type: string;
  is_active: boolean;
  sort_order?: number;
  price?: number;
  currency?: string;
  metadata?: string;
  schedule_from?: string;
  schedule_to?: string;
  icon_url?: string;
  thumbnail_url?: string;
  embed_code?: string;
}

/**
 * Transforms form data to API request format
 * Handles metadata construction and field mapping
 */
export function transformFormDataToApiRequest(formData: LinkFormData, pageId: string, sortOrder?: number): BioLinkRequestData {
  // Debug: Log what we received
  console.log('[transformFormDataToApiRequest] Received formData:', JSON.stringify(formData, null, 2));
  console.log('[transformFormDataToApiRequest] formData.type:', formData.type, '| type:', typeof formData.type);

  // Ensure type is always a valid string
  const linkType = formData.type && typeof formData.type === 'string' ? formData.type : 'LINK';
  console.log('[transformFormDataToApiRequest] Resolved linkType:', linkType);

  const requestData: BioLinkRequestData = {
    page_id: pageId,
    title: formData.title,
    url: formData.url,
    type: linkType,
    is_active: formData.isActive ?? true,
    sort_order: sortOrder ?? 0,
    price: formData.price,
    currency: formData.currency,
    icon_url: formData.iconUrl,
    thumbnail_url: formData.thumbnailUrl,
  };

  console.log('[transformFormDataToApiRequest] Final requestData:', JSON.stringify(requestData, null, 2));

  // Handle date fields
  if (formData.scheduleFrom) {
    requestData.schedule_from = formData.scheduleFrom.toISOString();
  }
  if (formData.scheduleTo) {
    requestData.schedule_to = formData.scheduleTo.toISOString();
  }

  // Construct metadata object for complex types
  const metadataObj: any = {};

  // Gated content metadata
  if (formData.gateType && formData.gateValue) {
    metadataObj.gatedContent = {
      type: formData.gateType,
      value: formData.gateValue,
      gateMessage: formData.gateMessage || '',
    };
  }

  // Embed content metadata
  if (formData.embedCode) {
    metadataObj.embedCode = formData.embedCode;
  }

  // Product metadata
  if (formData.type === 'PRODUCT' && formData.sku) {
    metadataObj.product = {
      sku: formData.sku,
      description: formData.description,
    };
  }

  // General description metadata
  if (formData.description && !metadataObj.product) {
    metadataObj.description = formData.description;
  }

  // Stringify metadata if it has content
  if (Object.keys(metadataObj).length > 0) {
    requestData.metadata = JSON.stringify(metadataObj);
  }

  return requestData;
}

/**
 * Validates form data based on link type
 * Returns validation errors or null if valid
 */
export function validateLinkFormData(formData: LinkFormData): Record<string, string> | null {
  const errors: Record<string, string> = {};

  // Basic validation
  if (!formData.title?.trim()) {
    errors.title = 'Title is required';
  }

  // Type-specific validation
  switch (formData.type) {
    case 'LINK':
      if (!formData.url?.trim()) {
        errors.url = 'URL is required for links';
      } else if (!isValidUrl(formData.url)) {
        errors.url = 'Please enter a valid URL';
      }
      break;

    case 'PRODUCT':
      if (!formData.price || formData.price <= 0) {
        errors.price = 'Price must be greater than 0';
      }
      if (!formData.currency?.trim()) {
        errors.currency = 'Currency is required';
      }
      break;

    case 'SCHEDULED':
      if (!formData.scheduleFrom) {
        errors.scheduleFrom = 'Start date is required for scheduled links';
      } else if (formData.scheduleTo && formData.scheduleFrom >= formData.scheduleTo) {
        errors.scheduleTo = 'End date must be after start date';
      }
      break;

    case 'GATED':
      if (!formData.gateType) {
        errors.gateType = 'Gate type is required';
      }
      if (!formData.gateValue?.trim()) {
        errors.gateValue = 'Gate value is required';
      }
      if (formData.gateType === 'email' && formData.gateValue && !isValidEmail(formData.gateValue)) {
        errors.gateValue = 'Please enter a valid email address';
      }
      break;

    case 'EMAIL':
      if (!formData.url?.trim()) {
        errors.url = 'Email address is required';
      } else if (formData.url && !isValidEmail(formData.url)) {
        errors.url = 'Please enter a valid email address';
      }
      break;

    case 'PHONE':
      if (!formData.url?.trim()) {
        errors.url = 'Phone number is required';
      } else if (formData.url && !isValidPhone(formData.url)) {
        errors.url = 'Please enter a valid phone number';
      }
      break;

    case 'EMBED':
      if (!formData.embedCode?.trim()) {
        errors.embedCode = 'Embed code is required';
      }
      break;
  }

  // URL validation for types that use it
  if (formData.url && ['LINK', 'SOCIAL'].includes(formData.type) && !isValidUrl(formData.url)) {
    errors.url = 'Please enter a valid URL';
  }

  return Object.keys(errors).length > 0 ? errors : null;
}

/**
 * URL validation helper
 */
function isValidUrl(url: string): boolean {
  try {
    const urlObj = new URL(url);
    return urlObj.protocol === 'http:' || urlObj.protocol === 'https:';
  } catch {
    return false;
  }
}

/**
 * Email validation helper
 */
function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Phone validation helper (basic)
 */
function isValidPhone(phone: string): boolean {
  const phoneRegex = /^\+?[\d\s\-\(\)]+$/;
  return phoneRegex.test(phone) && phone.replace(/\D/g, '').length >= 10;
}

/**
 * Transforms API response back to form data format
 */
export function transformApiResponseToFormData(apiData: any): LinkFormData {
  const formData: LinkFormData = {
    title: apiData.title || '',
    url: apiData.url || '',
    type: apiData.type || 'LINK',
    isActive: apiData.is_active ?? true,
    price: apiData.price,
    currency: apiData.currency,
    thumbnailUrl: apiData.thumbnail_url,
    iconUrl: apiData.icon_url,
  };

  // Parse dates
  if (apiData.schedule_from) {
    formData.scheduleFrom = new Date(apiData.schedule_from);
  }
  if (apiData.schedule_to) {
    formData.scheduleTo = new Date(apiData.schedule_to);
  }

  // Parse metadata
  if (apiData.metadata) {
    try {
      const metadata = JSON.parse(apiData.metadata);

      // Extract flattened fields from metadata
      if (metadata.gatedContent) {
        formData.gateType = metadata.gatedContent.type;
        formData.gateValue = metadata.gatedContent.value;
        formData.gateMessage = metadata.gatedContent.gateMessage;
      }

      if (metadata.embedCode) {
        formData.embedCode = metadata.embedCode;
      }

      if (metadata.product) {
        formData.sku = metadata.product.sku;
        formData.description = metadata.product.description;
      }

      if (metadata.description && !formData.description) {
        formData.description = metadata.description;
      }
    } catch {
      // Silent fail for metadata parsing
    }
  }

  return formData;
}
