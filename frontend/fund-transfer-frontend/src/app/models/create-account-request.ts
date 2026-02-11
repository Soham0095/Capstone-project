/**
 * SignupRequest model matching backend DTO
 * Maps to: com.example.dto.CreateAccountRequest
 */
export interface SignupRequest {
  holderName: string;
  username: string;
  password: string;
}

/**
 * API Response for signup
 */
export interface ApiResponse {
  message: string;
  success?: boolean;
}
