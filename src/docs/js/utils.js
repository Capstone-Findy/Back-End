// HTTP URL Path 복사 기능
function addCopyButtons() {
    const codeBlocks = document.querySelectorAll('pre > code[data-lang="http"]');
    codeBlocks.forEach((codeBlock) => {
        const preElement = codeBlock.parentElement;
        if (preElement.querySelector('.copy-url-btn')) return;
        preElement.classList.add('pre-http');
        const content = codeBlock.textContent;
        const match = content.match(/^(GET|POST|PUT|DELETE|PATCH)\s+([^\s\n]+)(?=\s|$)/);
        if (!match) {
            return;
        }
        const [fullMatch, method, fullUrl] = match;
        const [path, query] = fullUrl.split('?');
        const queryPart = query ? `?${query}` : '';
        const originalText = codeBlock.innerHTML;
        const newText = originalText.replace(fullMatch, `<span class="http-method ${method.toLowerCase()}">[${method}]</span> <span class="http-path">${path}</span><span class="http-query">${queryPart}</span>`);
        codeBlock.innerHTML = newText;
        const url = fullUrl;
        const copyBtn = document.createElement('button');
        copyBtn.textContent = 'Copy Path';
        copyBtn.className = 'copy-url-btn';
        copyBtn.onclick = function (e) {
            e.preventDefault();
            e.stopPropagation();
            const textArea = document.createElement('textarea');
            textArea.value = url;
            document.body.appendChild(textArea);
            textArea.select();
            try {
                document.execCommand('copy');
                copyBtn.textContent = 'Copied!';
                setTimeout(() => {
                    copyBtn.textContent = 'Copy URL';
                }, 1500);
            } catch (err) {
                console.error('Copy failed:', err);
                copyBtn.textContent = 'Failed!';
            } finally {
                document.body.removeChild(textArea);
            }
        };
        preElement.appendChild(copyBtn);
    });
}

// TypeScript 인터페이스 복사 기능
function addInterfaceCopyButton() {
    const tables = document.querySelectorAll('table.tableblock');
    tables.forEach(table => {
        const wrapper = table.parentElement;
        if (!wrapper.querySelector('.copy-interface-btn')) {
            wrapper.classList.add('table-wrapper');
            const copyBtn = document.createElement('button');
            copyBtn.textContent = 'Copy Interface';
            copyBtn.className = 'copy-interface-btn';
            copyBtn.onclick = function (e) {
                e.preventDefault();
                e.stopPropagation();
                const interfaceText = generateTypeScriptInterface(table);
                const textArea = document.createElement('textarea');
                textArea.value = interfaceText;
                document.body.appendChild(textArea);
                textArea.select();
                try {
                    document.execCommand('copy');
                    copyBtn.textContent = 'Copied!';
                    setTimeout(() => {
                        copyBtn.textContent = 'Copy Interface';
                    }, 1500);
                } catch (err) {
                    console.error('Copy failed:', err);
                    copyBtn.textContent = 'Failed!';
                    setTimeout(() => {
                        copyBtn.textContent = 'Copy Interface';
                    }, 1500);
                } finally {
                    document.body.removeChild(textArea);
                }
            };
            wrapper.appendChild(copyBtn);
        }
    });
}

function generateTypeScriptInterface(table) {
    let interfaceText = 'interface Test {\n';
    const rows = table.querySelectorAll('tbody tr');
    const processedFields = new Set();
    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length >= 2) {
            let fieldName = cells[0].querySelector('code')?.textContent || cells[0].textContent.trim();
            const typeCell = cells[1].textContent.trim();
            const isRequired = typeCell.includes('*');
            let type = typeCell.replace(/\s*\*\s*$/, '').trim();
            if (fieldName.includes('[]')) {
                const baseFieldName = fieldName.split('[')[0];
                if (!processedFields.has(baseFieldName)) {
                    interfaceText += `  ${baseFieldName}: Array<{\n`;
                    const subFields = Array.from(rows)
                        .filter(r => r.querySelector('td:first-child code')?.textContent?.startsWith(`${baseFieldName}[].`))
                        .map(r => {
                            const subFieldName = r.querySelector('td:first-child code').textContent.split('.')[1];
                            const subType = r.querySelector('td:nth-child(2)').textContent.trim();
                            const subIsRequired = subType.includes('*');
                            return `    ${subFieldName}${subIsRequired ? '' : '?'}: ${convertToTypeScriptType(subType.replace('*', '').trim())};`;
                        });
                    interfaceText += subFields.join('\n') + '\n  }>;\n';
                    processedFields.add(baseFieldName);
                }
            } else if (!fieldName.includes('.') && !processedFields.has(fieldName)) {
                interfaceText += `  ${fieldName}${isRequired ? '' : '?'}: ${convertToTypeScriptType(type)};\n`;
                processedFields.add(fieldName);
            }
        }
    });
    interfaceText += '}';
    return interfaceText;
}

function convertToTypeScriptType(type) {
    const typeMap = {
        'String': 'string', 'Number': 'number', 'Boolean': 'boolean', 'Array': 'Array<any>', 'Object': 'object'
    };
    return typeMap[type] || type.toLowerCase();
}
// 초기 실행
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        addCopyButtons();
        addInterfaceCopyButton();

        // DOM이 로드된 후에 MutationObserver 설정
        const observer = new MutationObserver((mutations) => {
            for (const mutation of mutations) {
                if (mutation.addedNodes.length) {
                    setTimeout(() => {
                        addCopyButtons();
                        addInterfaceCopyButton();
                    }, 100);
                }
            }
        });
        // document.body가 정상적으로 존재하는지 확인 후 observer 실행
        if (document.body) {
            observer.observe(document.body, {
                childList: true,
                subtree: true
            });
        } else {
            console.error('document.body가 존재하지 않습니다.');
        }
    });
} else {
    addCopyButtons();
    addInterfaceCopyButton();

    // DOM이 이미 로드된 상태에서 MutationObserver 설정
    const observer = new MutationObserver((mutations) => {
        for (const mutation of mutations) {
            if (mutation.addedNodes.length) {
                setTimeout(() => {
                    addCopyButtons();
                    addInterfaceCopyButton();
                }, 100);
            }
        }
    });
    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
}

