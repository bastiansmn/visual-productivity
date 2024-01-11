export interface Error {
  timestamp: Date,
  message: string,
  devMessage: string,
  httpStatusString: string
  httpStatus: Number
}
