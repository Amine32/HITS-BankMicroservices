export default function generateIdempotencyKey() {
    return crypto.randomUUID();
  }