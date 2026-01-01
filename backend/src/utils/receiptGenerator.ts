export function generateReceipt({ verified, recycledSince, reasonCode }: any) {
  return {
    receiptId: `CR-${Date.now()}`,
    verified,
    recycledSince,
    reasonCode,
    timestamp: new Date().toISOString(),
  };
}
