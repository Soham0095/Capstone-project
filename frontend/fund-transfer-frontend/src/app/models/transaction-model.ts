export interface Transaction {
    id: number;
    fromAccountId: number;
    fromHolderName: string;
    toAccountId: number;
    toHolderName: string;
    amount: number;
    createdOn: string;
    status: 'SUCCESS' | 'FAILURE';
    failureReason?: string;
    idempotencyKey?: string;
}