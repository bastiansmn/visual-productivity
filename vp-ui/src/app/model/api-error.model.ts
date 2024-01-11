export interface ApiError {
  timestamp: Date,
  message: string,
  devMessage: string,
  httpStatusString: string
  httpStatus: number
}
