/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.banking.psdx.interfaces.enums;

/**
 * Enum representing the type of Third Party Provider.
 */
public enum ProviderType {
    AISP, // Account Information Service Provider
    PISP, // Payment Initiation Service Provider
    CBPII, // Card Based Payment Instrument Issuer
    ASPSP // Account Servicing Payment Service Provider
}
