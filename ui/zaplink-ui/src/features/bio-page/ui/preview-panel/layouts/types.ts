import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { Variants } from "framer-motion";

export interface LayoutProps {
    page: BioPageWithTheme;
    previewMode?: boolean;
    socialLinks: any[];
    regularLinks: any[];
    portalLinks: any[];
    itemVariants: Variants;
}
