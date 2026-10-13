
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 20 },
        { duration: '2m', target: 20 }
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<1000'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8085';
const CLIENT_ID = '00000000-0000-4000-8000-000000000001';

const params = {
    headers: {
        'Content-Type': 'application/json',
    },
};

export default function () {
    // 1. Create application
    const createResponse = http.post(
        `${BASE_URL}/api/applications`,
        JSON.stringify({
            clientId: CLIENT_ID,
            status: 'KYC_PENDING',
            kycSessionId: `session-${__VU}-${__ITER}`,
        }),
        params,
    );

    const createOk = check(createResponse, {
        'create returned 2xx': (r) => r.status >= 200 && r.status < 300,
    });

    if (!createOk) {
        console.error(
            `CREATE FAILED: status=${createResponse.status}, body=${createResponse.body}`,
        );
        return;
    }

    let applicationId;

    try {
        const body = JSON.parse(createResponse.body);
        applicationId = body.id;
    } catch (e) {
        console.error(`Invalid create response JSON: ${createResponse.body}`);
        return;
    }

    if (!applicationId) {
        console.error(`Missing id in create response: ${createResponse.body}`);
        return;
    }

    // 2. Complete KYC using the ID returned by create
    const kycResponse = http.post(
        `${BASE_URL}/api/applications/complete-kyc`,
        JSON.stringify({
            applicationId: applicationId,
            clientId: CLIENT_ID,
        }),
        params,
    );

    const kycOk = check(kycResponse, {
        'KYC completion returned 2xx': (r) =>
            r.status >= 200 && r.status < 300,
    });

    if (!kycOk) {
        console.error(
            `KYC FAILED: applicationId=${applicationId}, status=${kycResponse.status}, body=${kycResponse.body}`,
        );
    }

    sleep(1);
}
