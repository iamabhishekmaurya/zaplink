import api from '../../utils/api';

export interface QRConfigType {
    data: string;
    size: number;
    margin: number;
    errorCorrectionLevel: string;
    transparentBackground: boolean;
    backgroundColor: string;
    body: {
        shape: string;
        color: string;
        colorDark?: string;
        gradientLinear: boolean;
    };
    eye: {
        shape: string;
        colorOuter: string;
        colorInner: string;
    };
    logo?: {
        logoPath?: string;
        sizeRatio: number;
        padding: number;
        backgroundColor: string;
        backgroundEnabled: boolean;
        backgroundRounded: boolean;
        backgroundCornerRadius: number;
        removeQuietZone: boolean;
        marginSize: number;
    };
}

const API_BASE_URL = '/v1/api/qr/advanced';

export const QRApi = {
    generateStyledQR: async (config: QRConfigType): Promise<string> => {
        const response = await api.post(`${API_BASE_URL}/styled`, config, {
            responseType: 'blob'
        });
        return URL.createObjectURL(response.data);
    }
};
