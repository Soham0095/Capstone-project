export interface Account {
    id: number;
    username: string;
    holderName: string;
    balance: number;
    status: string;
    version?: number;
    rewardPoints?: number;
}