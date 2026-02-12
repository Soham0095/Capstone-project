export interface Transaction {
    id: number;
    fromAccountId: number;
    toAccountId: number;
    amount: number;
    createdOn: string;
    status: 'SUCCESS' | 'FAILURE';
    failureReason?: string;
    idempotencyKey?: string;
}