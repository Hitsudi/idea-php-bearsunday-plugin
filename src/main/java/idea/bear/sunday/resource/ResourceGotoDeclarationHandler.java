package idea.bear.sunday.resource;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.psi.elements.impl.StringLiteralExpressionImpl;
import idea.bear.sunday.index.ResourceIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceGotoDeclarationHandler implements GotoDeclarationHandler {

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int offset, Editor editor) {

        if (psiElement == null) {
            return new PsiElement[0];
        }
        if (!(psiElement instanceof LeafPsiElement leafPsiElement)) {
            return new PsiElement[0];
        }

        PsiElement context = psiElement.getContext();
        if (!(context instanceof StringLiteralExpressionImpl stringLiteralExpression)) {
            return new PsiElement[0];
        }

        String resourceName = stringLiteralExpression.getContents();
        if (leafPsiElement.getElementType().equals(PhpDocTokenTypes.DOC_STRING)) {
            resourceName = resourceName.replaceAll("\"", "");
        }

        if(!resourceName.startsWith("app://") && !resourceName.startsWith("page://")) {
            if (resourceName.startsWith("/")){

                String schema = "app";
                if  (((EditorImpl) editor).getVirtualFile().getPath().startsWith(
                    psiElement.getProject().getBasePath() + "/src/Resource/Page")
                ){
                    schema = "page";
                }
                resourceName = schema + "://self" + resourceName;
            } else {
                return new PsiElement[0];
            }
        }

        return ResourceIndex.getFileByUri(resourceName, psiElement.getProject(), editor);
    }

    @Nullable
    @Override
    public String getActionText(@NotNull DataContext dataContext) {
        return "Go to Resource class";
    }

}