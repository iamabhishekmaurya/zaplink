import { API_ENDPOINTS } from "@/lib/constants/apiConstant";
import { QRConfigType } from "@/lib/types/apiRequestType";
import api from "@/services/client";

export class QRApiService {
	/**
	 * Generate a styled QR code
	 * @param config - QR code configuration
	 * @returns Promise<string> - URL of the generated QR code
	 */
	static async generateStyledQR(config: QRConfigType): Promise<string> {
		const response = await api.post(
			API_ENDPOINTS.GENERATE_STYLED_QR,
			config,
			{ responseType: "blob" }
		);
		return URL.createObjectURL(response.data);
	}

	/**
	 * Generate a simple QR code
	 * @param data - Data to encode in QR code
	 * @param size - QR code size
	 * @returns Promise<string> - URL of the generated QR code
	 */
	static async generateSimpleQR(
		data: string,
		size: number = 200
	): Promise<string> {
		const response = await api.post(
			API_ENDPOINTS.GENERATE_SIMPLE_QR,
			{ data, size },
			{ responseType: "blob" }
		);
		return URL.createObjectURL(response.data);
	}
}

// Export an instance of the class for easy usage
export const QRService = QRApiService;
