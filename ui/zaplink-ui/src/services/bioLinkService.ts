// This file re-exports bio link functionality from bioPageService for backwards compatibility
// All bio link operations are now consolidated in bioPageService.ts

export {
  bioPageService as bioLinkService,
  type BioLink,
  type CreateBioLinkRequest,
  type UpdateBioLinkRequest,
  type ReorderLinksRequest
} from './bioPageService';
