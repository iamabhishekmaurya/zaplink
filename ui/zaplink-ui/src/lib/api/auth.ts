import { API_ENDPOINTS } from "../constant/apiConstant";
import {
    BaseResponse,
    ForgotPasswordRequest,
    LoginRequest,
    LoginResponse,
    RegisterRequest,
    ResetPasswordRequest,
    UserInfo,
    UserRegistrationResponse,
    VerifyEmailResponse
} from "../types/apiRequestType";
import api from "../util/api";

export const AuthApi = {
    login: async (data: LoginRequest): Promise<LoginResponse> => {
        const response = await api.post<LoginResponse>(API_ENDPOINTS.LOGIN, data);
        return response.data;
    },

    register: async (data: RegisterRequest): Promise<UserRegistrationResponse> => {
        const response = await api.post<UserRegistrationResponse>(API_ENDPOINTS.REGISTER, data);
        return response.data;
    },

    forgotPassword: async (data: ForgotPasswordRequest): Promise<BaseResponse> => {
        const response = await api.post<BaseResponse>(API_ENDPOINTS.REQUEST_PASSWORD_RESET, data);
        return response.data;
    },

    verifyEmail: async (token: string): Promise<VerifyEmailResponse> => {
        const response = await api.post<VerifyEmailResponse>(API_ENDPOINTS.VERIFY_EMAIL, null, { params: { token } });
        return response.data;
    },

    resendVerification: async (email: string): Promise<BaseResponse> => {
        const response = await api.post<BaseResponse>(API_ENDPOINTS.RESEND_VERIFICATION, null, { params: { email } });
        return response.data;
    },

    resetPassword: async (data: ResetPasswordRequest): Promise<BaseResponse> => {
        const { token, newPassword } = data;
        const response = await api.post<BaseResponse>(API_ENDPOINTS.RESET_PASSWORD, null, { params: { token, newPassword } });
        return response.data;
    },

    getCurrentUser: async (): Promise<UserInfo> => {
        const response = await api.get<UserInfo>(API_ENDPOINTS.GET_CURRENT_USER);
        return response.data;
    }
};

export default AuthApi;
