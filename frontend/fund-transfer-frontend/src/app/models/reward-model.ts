export interface RewardLedger {
  id: number;
  pointsEarned: number;
  transactionAmount: number;
  transactionLogId: number;
  createdOn: string;
}

export interface RewardSummary {
  totalPoints: number;
  history: RewardLedger[];
}
